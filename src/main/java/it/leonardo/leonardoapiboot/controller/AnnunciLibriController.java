package it.leonardo.leonardoapiboot.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.form.CreateAnnuncioForm;
import it.leonardo.leonardoapiboot.service.*;
import it.leonardo.leonardoapiboot.utils.AnnunciComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
            @ApiResponse(responseCode = "409", description = "È già presente l'annuncio specificato nel carrello dell'utente."),
            @ApiResponse(responseCode = "404", description = "Alcuni dei campi specificati sono inesistenti. Maggiori informazioni nell'attributo 'invalidField' del body json di risposta"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @PostMapping
    public ResponseEntity<Object> insert(CreateAnnuncioForm form) {
        /*log.info("Invoked AnnunciLibriController.insert(" + form + ")");
        AnnunciLibri al = new AnnunciLibri();
        Optional<Libro> optLibr;
        Optional<Citta> optCitt;
        try {
            optLibr = libroService.findByIsbn(isbn);
            if (!optLibr.isPresent()) {

            }

            optCitt = cittaService.getById(citta);
            if (!optCitt.isPresent())
                return new ResponseEntity<>("{\"invalidField\" : \"citta\"}", HttpStatus.NOT_FOUND);

            al.setLibro(optLibr.get());
            al.setCitta(optCitt.get());
            al.setPrezzo(prezzo);
            al.setCreated_at(new GregorianCalendar().getTime());
            al.setLivello_usura(livello_usura);
            al.setStato(1);

            String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

            if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            al.setUtente(utentePublicInfoService.getById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            AnnunciLibri alSaved = service.save(al);
            alSaved.setUtente(null);
            alSaved.getLibro().setAnnunci(null);
            return new ResponseEntity<>(alSaved, HttpStatus.CREATED);


        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }*/
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(description = "Verifica se un libro è presente nel database, dato il suo isbn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, il libro è presente e le informazioni sono restituite"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "404", description = "Il libro non è presente nel database"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping("/present")
    public ResponseEntity<Libro> isPresent(@RequestParam("isbn") String isbn) {
        log.info("Invoked AnnunciLibriController.isPresent(" + isbn + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();
        Optional<Libro> opt = libroService.findByIsbn(isbn);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(opt.get());
    }

    @Operation(description = "Verifica la correttezza di una query, restituendo una o più possibilità")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la lista delle possibilità è stata popolata"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "404", description = "Non sono stati ottenuti riscontri dalle API di Google Books"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping("/verify")
        public ResponseEntity<String> verify(@RequestParam("q") String q) {
        log.info("Invoked AnnunciLibriController.verify(" + q + ")");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();

        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + q;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JSONObject json = new JSONObject(response);
            if (json.getInt("totalItems") != 0) {
                JSONArray arr = new JSONArray();
                arr.put(json.getJSONArray("items").getJSONObject(0));
                return ResponseEntity.ok(arr.toString());
            }

            url = "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(q, "UTF-8");
            response = restTemplate.getForObject(url, String.class);

            json = new JSONObject(response);
            if (json.getInt("totalItems") == 0) return ResponseEntity.notFound().build();
            JSONArray items = json.getJSONArray("items");
            JSONArray tmp = new JSONArray();
            for (int i = 0; i < Math.min(items.length(), 10); i++) {
                tmp.put(items.getJSONObject(i));
            }

            return ResponseEntity.ok(tmp.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Aggiorna un annuncio, impostando come utente l'utente ricavato dalla sessione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, l'annuncio è stato aggiornato."),
            @ApiResponse(responseCode = "404", description = "Alcuni dei campi specificati sono inesistenti. Maggiori informazioni nell'attributo 'invalidField' del body json di risposta"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "403", description = "Non si è proprietari dell'annuncio, e di conseguenza non è possibile modificarlo"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")})
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestParam(required = false) Float prezzo, @RequestParam(required = false) Integer citta, @RequestParam(required = false) Integer stato, @RequestParam(required = false) Character livello_usura) {
        log.info("Invoked AnnunciLibriController.update(" + id + ", " + prezzo + ", " + citta + ", " + stato + ", " + livello_usura + ")");
        /*String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            Optional<AnnunciLibri> opt = service.findById(id);
            if (!opt.isPresent()) return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);

            AnnunciLibri ann = opt.get();
            if (ann.getUtente().equals(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get())) {
                if (prezzo != null) ann.setPrezzo(prezzo);

                if (citta != null) {
                    Optional<Citta> c = cittaService.getById(citta);
                    if (!c.isPresent())
                        return new ResponseEntity<>("{\"invalidField\" : \"citta\"}", HttpStatus.NOT_FOUND);
                    ann.setCitta(c.get());
                }

                if (stato != null) ann.setStato(stato);
                if (livello_usura != null) ann.setLivello_usura(livello_usura);

                AnnunciLibri annUpdated = service.save(ann);
                annUpdated.setUtente(null);
                return ResponseEntity.ok(annUpdated);

            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }*/
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }
}
