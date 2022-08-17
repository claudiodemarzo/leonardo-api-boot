package it.leonardo.leonardoapiboot.controller;


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
import org.apache.coyote.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.*;

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
    private CittaService cittaService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

    @Autowired
    private StatusLibroService statusLibroService;

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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
                lstAnn.sort(new AnnunciComparator());
                Collections.reverse(lstAnn);
                libro.setAnnunci(lstAnn);
            });
            return ResponseEntity.ok(lst);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
        try {
            Optional<AnnunciLibri> opt = service.findById(id);
            if (!opt.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            AnnunciLibri al = opt.get();
            Optional<Libro> lOpt = libroService.findByIsbn(al.getLibro().getIsbn());

            if (!lOpt.isPresent()) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            Libro l = lOpt.get();
            if (!all) {
                l.getAnnunci().removeIf(ann -> ann.getAnnuncio_id().intValue() != id);
            }
            return new ResponseEntity<>(l, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(description = "Restituisce una lista di tutti gli annunci pubblicati dall'utente il quale id è fornito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la lista degli annunci è stata popolata"),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è risultata essere vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @GetMapping("/by-utente/{id}")
    public ResponseEntity<Object> getByUtente(@PathVariable("id") Integer id) {
        log.info("Invoked AnnunciLibriController.getByUtente(" + id + ")");
        try {
            Optional<Utente> opt = utenteService.findById(id);
            if (!opt.isPresent()) return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            List<AnnunciLibri> lst = service.getByUtente(opt.get());
            if (lst.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            for (AnnunciLibri a : lst) {
                a.getUtente().setPunti(null);
            }
            return new ResponseEntity<>(lst, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            lst.sort(new AnnunciComparator());
            Collections.reverse(lst);
            lst.forEach(a -> {
                a.getUtente().setPunti(null);
            });
            if (lst.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(lst, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(description = "Restituisce una lista di tutti gli annunci taggati con la città fornita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la lista degli annunci è stata popolata"),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è risultata essere vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @GetMapping("/by-citta/{id}")
    public ResponseEntity<Object> getByCitta(@PathVariable("id") Integer id) {
        log.info("Invoked AnnunciLibriController.getByCitta(" + id + ")");

        try {
            Optional<Citta> opt = cittaService.getById(id);
            if (!opt.isPresent()) return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);

            List<AnnunciLibri> lst = service.getByCitta(opt.get());
            if (lst.isEmpty()) return ResponseEntity.noContent().build();

            lst.forEach(a -> {
                a.getUtente().setPunti(null);
            });
            return new ResponseEntity<>(lst, HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

        Optional<Libro> opt = libroService.findByIsbn(form.getIsbn());
        Libro l = null;
        if (opt.isPresent()) l = opt.get();
        else {
            l = Libro.fromCreateAnnuncioForm(form);
        }
        StatusLibro sl = statusLibroService.getStatus(form.getSottCanc(), form.getSottNonCanc(), form.getScrittCanc(), form.getScrittNonCanc(), form.getPagManc(), form.getPagRov(), form.getPagRovMol(), form.getCopRov(), form.getInsManc()).get();
        AnnunciLibri a = AnnunciLibri.fromCreateAnnuncioForm(l, utentePublicInfoService.getById(Integer.parseInt(userid)).get(), sl, form);

        if (a.getLivello_usura() == 'x') return ResponseEntity.badRequest().build();

        AnnunciLibri alSaved = service.save(a);
        return new ResponseEntity<>(alSaved, HttpStatus.CREATED);
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
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Imposta un annuncio come venduto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, l'annuncio è stato impostato come venduto."),
            @ApiResponse(responseCode = "404", description = "L'annuncio non esiste/non è di proprietà dell'utente"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server"),
            @ApiResponse(responseCode = "409", description = "L'annuncio è già stato impostato come venduto")
    })
    @PostMapping("{id}/sold")
    public ResponseEntity<Object> setSold(@PathVariable Integer id) {
        log.info("Invoked AnnunciLibriController.setSold(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            Optional<AnnunciLibri> opt = service.findById(id);
            if (!opt.isPresent()) return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            AnnunciLibri ann = opt.get();
            if (ann.getUtente().getId().equals(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get().getUtenteId())) {
                if (ann.getStato() == 1) return new ResponseEntity<>(HttpStatus.CONFLICT);
                ann.setStato(1);
                AnnunciLibri annUpdated = service.save(ann);
                return ResponseEntity.ok(annUpdated);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/contact")
    public ResponseEntity<Object> contact(Integer id) {
        log.info("Invoked AnnunciLibriController.contact(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            Optional<AnnunciLibri> opt = service.findById(id);
            if (!opt.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            AnnunciLibri ann = opt.get();
            if (ann.getUtente().getId().equals(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get().getUtenteId())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ChatWSController.sendNotification(ann.getUtente().getId().toString(), new Notifica(Notifica.TipoNotifica.info, "Richiesta di contatto", "@"+utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get().getUsername()+ " è interessato al tuo annuncio: "+ann.getLibro().getNome()));
            return ResponseEntity.ok().body("{}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
