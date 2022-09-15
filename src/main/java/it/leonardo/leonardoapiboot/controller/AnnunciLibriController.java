package it.leonardo.leonardoapiboot.controller;


import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.*;
import it.leonardo.leonardoapiboot.entity.form.CreateAnnuncioForm;
import it.leonardo.leonardoapiboot.entity.form.UpdateAnnuncioForm;
import it.leonardo.leonardoapiboot.service.*;
import it.leonardo.leonardoapiboot.utils.AnnunciComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.Instant;
import java.util.*;

import static it.leonardo.leonardoapiboot.utils.ImageUtils.downloadFile;
import static it.leonardo.leonardoapiboot.utils.ImageUtils.encodeWebp;

@RestController
@RequestMapping("/annuncilibri")
//@CrossOrigin(origins = {"http://leonardostart.ddns.net","http://127.0.0.1:5507","http://192.168.1.2"}, allowCredentials = "true")
@CrossOrigin(origins = "https://leonardostart.tk", allowCredentials = "true")
public class AnnunciLibriController {

    private final Log log = LogFactory.getLog(AnnunciLibriController.class);

    @Autowired
    private AnnunciLibriService service;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

    @Autowired
    private StatusLibroService statusLibroService;

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private MessaggioService messaggioService;

    @Autowired
    private NotificaService notificaService;

    @Autowired
    private CarrelloService carrelloService;

    @Operation(description = "Restituisce tutti gli annunci")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la lista è stata popolata "),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping
    public ResponseEntity<List<AnnunciLibri>> getAll() {
        log.info("Invoked AnnunciLibriController.getAll()");
        List<AnnunciLibri> lst;
        try {
            lst = service.getAll();
            if (lst.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(lst, HttpStatus.OK);


        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Restituisce una lista di massimo 10 libri, i quali annunci sono tra i più recenti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping("/last/{amount}")
    public ResponseEntity<List<Libro>> getLast(@PathVariable Integer amount) {
        log.info("Invoked AnnunciLibriController.getLast(" + amount + ")");
        try {
            List<Libro> lst = libroService.findFirstLimit(amount);
            lst.forEach(libro -> {
                List<AnnunciLibri> lstAnn = new ArrayList<>(libro.getAnnunci());
                lstAnn.removeIf(ann -> ann.getStato() != 1);
                lstAnn.removeIf(ann -> session.getAttribute("userID") != null && ann.getUtente().getId() == Integer.parseInt(session.getAttribute("userID").toString()));
                lstAnn.sort(new AnnunciComparator());
                Collections.reverse(lstAnn);
                libro.setAnnunci(lstAnn);
            });
            lst.removeIf(l -> l.getAnnunci().isEmpty());
            return ResponseEntity.ok(lst);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Restituisce l'annuncio il quale id è fornito, con o senza una lista degli annunci relativi al libro al quale quell'annuncio fa riferimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine"),
            @ApiResponse(responseCode = "404", description = "L'annuncio specificato non è stato trovato"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getById(@PathVariable Integer id, @RequestParam(required = false, name = "all") Optional<Boolean> allOpt) {
        log.info("Invoked AnnunciLibriController.getById(" + id + ", " + allOpt + ")");
        boolean all = (allOpt.orElse(true));

        String userID = session.getAttribute("userID") != null ? session.getAttribute("userID").toString() : null;

        try {
            Optional<AnnunciLibri> opt = service.findById(id);
            if (!opt.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            AnnunciLibri al = opt.get();
            Optional<Libro> lOpt = libroService.findByIsbn(al.getLibro().getIsbn());

            if (!lOpt.isPresent())
                return ResponseEntity.internalServerError().build();

            Libro l = lOpt.get();
            if (!all) {
                l.getAnnunci().removeIf(ann -> ann.getStato() != 1 && (userID == null || ann.getUtente().getId() != Integer.parseInt(userID)));
            } else {
                if(al.getStato() != 1 && (userID == null || al.getUtente().getId() != Integer.parseInt(userID)))
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            int index = l.getAnnunci().indexOf(al);

            al =  l.getAnnunci().remove(index);

            l.getAnnunci().add(0, al);

            return new ResponseEntity<>(l, HttpStatus.OK);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Restituisce una lista di tutti gli annunci pubblicati dall'utente il quale id è fornito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la lista degli annunci è stata popolata"),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è risultata essere vuota"),
            @ApiResponse(responseCode = "404", description = "L'utente specificato non è stato trovato"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @GetMapping("/by-utente/{id}")
    public ResponseEntity<Object> getByUtente(@PathVariable("id") Integer id) {
        log.info("Invoked AnnunciLibriController.getByUtente(" + id + ")");
        try {
            Optional<UtentePublicInfo> opt = utentePublicInfoService.getById(id);
            if (!opt.isPresent()) return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            List<AnnunciLibri> lst = service.getByUtente(opt.get());
            List<String> lstIsbn = new ArrayList<>();
            lst.forEach(al -> lstIsbn.add(al.getLibro().getIsbn()));
            List<Libro> lstLibri = new ArrayList<>();
            for (String isbn : lstIsbn) {
                Optional<Libro> optLibro = libroService.findByIsbn(isbn);
                if (optLibro.isPresent()) lstLibri.add(optLibro.get());
            }
            List<Libro> actualLstLibri = new ArrayList<>();
            for (Libro l : lstLibri) {
                boolean found = false;
                for (AnnunciLibri al : lst) {
                    if (al.getUtente().getId().equals(id) && al.getLibro().getIsbn().equals(l.getIsbn()) && al.getStato() != 3) {
                        List<AnnunciLibri> tmp = l.getAnnunci().stream().filter(all -> all.getUtente().getId().equals(id)).toList();
                        l.setAnnunci(tmp);
                        actualLstLibri.add(l);
                        found = true;
                    }
                    if (found) break;
                }
            }
            if (actualLstLibri.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(actualLstLibri, HttpStatus.OK);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }

    }

    @Operation(description = "Restituisce una lista di tutti gli annunci relativi a libri che hanno l'isbn fornito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la lista degli annunci è stata popolata"),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è risultata essere vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @GetMapping("/by-isbn/{id}")
    public ResponseEntity<Object> getByIsbn(@PathVariable("id") String id) {
        log.info("Invoked AnnunciLibriController.getByLibro(" + id + ")");
        try {
            Optional<Libro> opt = libroService.findByIsbn(id);
            if (!opt.isPresent()) return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);

            List<AnnunciLibri> lst = service.getByLibro(opt.get());
            lst.removeIf(al -> al.getStato() == 3);
            lst.sort(new AnnunciComparator());
            Collections.reverse(lst);
            lst.forEach(a -> {
                a.getUtente().setPunti(null);
            });
            if (lst.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(lst, HttpStatus.OK);

        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }

    }

    @Operation(description = "Inserisce un annuncio, impostando come utente l'utente ricavato dalla sessione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "La richiesta è andata a buon fine, l'annuncio è stato creato."),
            @ApiResponse(responseCode = "400", description = "Alcuni dei campi specificati sono non validi. Maggiori informazioni nell'attributo 'invalidField' del body json di risposta, oppure l'annuncio non è pubblicabile a causa del suo stato di usura."),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @PostMapping
    public ResponseEntity<Object> insert(CreateAnnuncioForm form) {
        log.info("Invoked AnnunciLibriController.insert(" + form + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();
        try {
            Optional<Libro> opt = libroService.findByIsbn(form.getIsbn());

            Libro l = null;
            if (opt.isPresent()) l = opt.get();
            else {
                l = Libro.fromCreateAnnuncioForm(form);
                String copertinaPath = "/var/www/html/assets/imgs/libri/" + l.getIsbn() + ".webp", tmpCopertinaPath = "/var/www/html/assets/imgs/libri/" + l.getIsbn() + ".tmp.webp";
                downloadFile(form.getImgLink(), tmpCopertinaPath);

                File tmpCopertinaFile = new File(tmpCopertinaPath);
                BufferedImage image = ImageIO.read(tmpCopertinaFile);
                BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

                imageCopy.getGraphics().drawImage(image, 0, 0, null);
                byte[] imageCopyBytes = ((DataBufferByte) imageCopy.getRaster().getDataBuffer()).getData();

                Mat imageMatrix = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
                imageMatrix.put(0, 0, imageCopyBytes);

                imageCopyBytes = encodeWebp(imageMatrix, 100);
                OutputStream webpOutputStream = new FileOutputStream(copertinaPath);
                webpOutputStream.write(imageCopyBytes);
                tmpCopertinaFile.delete();

                l.setCopertina(copertinaPath.replace("/var/www/html", ""));
                libroService.save(l);
            }
            StatusLibro sl = statusLibroService.getStatus(form.getSottCanc(), form.getSottNonCanc(), form.getScrittCanc(), form.getScrittNonCanc(), form.getPagManc(), form.getPagRov(), form.getPagRovMol(), form.getCopRov(), form.getInsManc()).get();
            AnnunciLibri a = AnnunciLibri.fromCreateAnnuncioForm(l, utentePublicInfoService.getById(Integer.parseInt(userid)).get(), sl, form);
            a.setCreated_at(Date.from(Instant.now()));

            if (a.getLivello_usura() == 'x') return ResponseEntity.badRequest().build();

            AnnunciLibri alSaved = service.save(a);

            return new ResponseEntity<>(alSaved, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(description = "Aggiorna un annuncio, impostando come utente l'utente ricavato dalla sessione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, l'annuncio è stato aggiornato."),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "404", description = "Non si è proprietari dell'annuncio, e di conseguenza non è possibile modificarlo, o non è stato trovato l'id dell'annuncio"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(UpdateAnnuncioForm form) {
        log.info("Invoked AnnunciLibriController.update(" + form + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            Optional<AnnunciLibri> opt = service.findById(form.getId());
            if (!opt.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            AnnunciLibri al = opt.get();
            if (al.getUtente().getId() != Integer.parseInt(session.getAttribute("userID").toString()))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            char newUsura = AnnunciLibri.calcolaLivelloUsura(form);
            if (newUsura == 'x') return ResponseEntity.badRequest().build();

            al.copyFromUpdateAnnuncioForm(form, statusLibroService.getStatus(form.getSottCanc(), form.getSottNonCanc(), form.getScrittCanc(), form.getScrittNonCanc(), form.getPagManc(), form.getPagRov(), form.getPagRovMol(), form.getCopRov(), form.getInsManc()).get());

            AnnunciLibri alSaved = service.save(al);
            return new ResponseEntity<>(alSaved, HttpStatus.OK);

        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Imposta un annuncio come venduto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, l'annuncio è stato impostato come venduto."),
            @ApiResponse(responseCode = "404", description = "L'annuncio non esiste/non è di proprietà dell'utente, o l'utente specificato non esiste"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server"),
            @ApiResponse(responseCode = "409", description = "L'annuncio è già stato impostato come venduto")
    })
    @PostMapping("/sold")
    public ResponseEntity<Object> setSold(Integer idAnn,Integer idUser) {
        log.info("Invoked AnnunciLibriController.setSold(" + idAnn + ", " + idUser + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            Optional<AnnunciLibri> opt = service.findById(idAnn);
            if (!opt.isPresent()) return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            AnnunciLibri ann = opt.get();
            if (ann.getUtente().getId().equals(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get().getUtenteId())) {
                if (ann.getStato() != 1) return new ResponseEntity<>(HttpStatus.CONFLICT);
                ann.setStato(2);

                Optional<UtentePublicInfo> optUser = utentePublicInfoService.getById(idUser);
                if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                ann.setSoldTo(optUser.get());
                ann.setSaleDate(Date.from(Instant.now()));
                AnnunciLibri annUpdated = service.save(ann);

                carrelloService.deleteByAnnuncio(annUpdated);
                return ResponseEntity.ok(annUpdated);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Invia una notifica al proprietario dell'annuncio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la notifica è stata inviata."),
            @ApiResponse(responseCode = "404", description = "L'annuncio non esiste/è di proprietà dell'utente stesso"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @PostMapping("/contact")
    public ResponseEntity<Object> contact(Integer id) {
        log.info("Invoked AnnunciLibriController.contact(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            Optional<AnnunciLibri> opt = service.findById(id);
            if (!opt.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            AnnunciLibri ann = opt.get();
            if (ann.getUtente().getId().equals(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get().getUtenteId()) || ann.getStato() != 1) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ChatWSController.sendNotification(ann.getUtente().getId().toString(), new Notifica(Notifica.TipoNotifica.info, "Richiesta di contatto", "@" + utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get().getUsername() + " è interessato al tuo annuncio: " + ann.getLibro().getNome(), utenteService.findById(ann.getUtente().getId()).get()), notificaService);

            BigDecimal costo = BigDecimal.valueOf(ann.getPrezzo());
            costo = costo.setScale(2, RoundingMode.HALF_UP);
            ChatWSController.sendMessage(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get(), utenteService.findById(ann.getUtente().getId()).get(), "Ciao! Sono interessato all'acquisto di un libro<div ad-id=\"" + ann.getAnnuncio_id() + "\" class=\"ad-contacted card flex-row w-100\"><img src=\"" + ann.getLibro().getCopertina() + "\" alt=\"" + ann.getLibro().getNome() + "\"><div class=\"card-body d-flex flex-column\"><div class=\"ad-title\">" + ann.getLibro().getNome() + "</div><div class=\"ad-isbn\">" + ann.getLibro().getIsbn() + "</div><div class=\"ad-description\">" + ann.getLibro().getDescrizione() + "</div><div class=\"ad-price\">Prezzo: € " + costo.toPlainString() + "</div></div></div>", chatroomService, utentePublicInfoService, messaggioService);

            return ResponseEntity.ok().body("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Elimina (nasconde) l'annuncio specificato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, l'annuncio è stato eliminato."),
            @ApiResponse(responseCode = "404", description = "L'annuncio non esiste/non è di proprietà dell'utente"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @DeleteMapping("/{idAnn}")
    public ResponseEntity<Object> delete(@PathVariable Integer idAnn) {
        log.info("Invoked AnnunciLibriController.delete(" + idAnn + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            Optional<AnnunciLibri> opt = service.findById(idAnn);
            if (!opt.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            AnnunciLibri ann = opt.get();
            if (ann.getUtente().getId().equals(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get().getUtenteId()) && ann.getStato() != 2) {
                ann.setStato(3);
                AnnunciLibri annUpdated = service.save(ann);
                carrelloService.deleteByAnnuncio(annUpdated);
                return ResponseEntity.ok(annUpdated);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
