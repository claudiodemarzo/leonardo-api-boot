package it.leonardo.leonardoapiboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.form.InsertLibroForm;
import it.leonardo.leonardoapiboot.service.LibroService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/libri")
@CrossOrigin(origins = "*")
public class LibroController {

    private final Log log = LogFactory.getLog(LibroController.class);

    @Autowired
    private LibroService service;

    @Autowired
    private HttpSession session;

    @GetMapping
    public ResponseEntity<List<Libro>> getAll() {
        log.info("Invoked LibroController.getAll()");
        ResponseEntity<List<Libro>> resp = null;

        try {
            List<Libro> lst = service.findAll();
            if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else resp = new ResponseEntity<>(lst, HttpStatus.OK);
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @GetMapping("{id}")
    public ResponseEntity<Libro> getById(@PathVariable("id") String isbn) {
        log.info("Invoked LibroController.getById(" + isbn + ")");
        ResponseEntity<Libro> resp = null;

        try {
            Optional<Libro> libro = service.findByIsbn(isbn);
            if (libro.isPresent()) resp = new ResponseEntity<>(libro.get(), HttpStatus.OK);
            else resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @GetMapping("/by-nome/{nome}")
    public ResponseEntity<List<Libro>> getByNome(@PathVariable("nome") String nome, @RequestParam(value = "searchType", required = false) Integer searchType) {
        log.info("Invoked LibroController.getByNome(" + nome + ", " + searchType + ")");
        ResponseEntity<List<Libro>> resp = null;

        //default to like search
        if (searchType == null) searchType = 2;

        switch (searchType) {
            case 1:     //exact
                try {
                    List<Libro> lst = service.findByExactNome(nome);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
            case 2:     //like
                try {
                    List<Libro> lst = service.findByLikeNome(nome);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
        }


        return resp;
    }

    @GetMapping("/by-autore/{nome}")
    public ResponseEntity<List<Libro>> getByAutore(@PathVariable("nome") String autore, @RequestParam(value = "searchType", required = false) Integer searchType) {
        log.info("Invoked LibroController.getByAutore(" + autore + ", " + searchType + ")");
        ResponseEntity<List<Libro>> resp = null;

        //default to like search
        if (searchType == null) searchType = 2;

        switch (searchType) {
            case 1:     //exact
                try {
                    List<Libro> lst = service.findByExactAutore(autore);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
            case 2:     //like
                try {
                    List<Libro> lst = service.findByLikeAutore(autore);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
        }


        return resp;
    }

    @GetMapping("/by-casaed/{nome}")
    public ResponseEntity<List<Libro>> getByCasaed(@PathVariable("nome") String casaed, @RequestParam(value = "searchType", required = false) Integer searchType) {
        log.info("Invoked LibroController.getByAutore(" + casaed + ", " + searchType + ")");
        ResponseEntity<List<Libro>> resp = null;

        //default to like search
        if (searchType == null) searchType = 2;

        switch (searchType) {
            case 1:     //exact
                try {
                    List<Libro> lst = service.findByExactCasaed(casaed);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
            case 2:     //like
                try {
                    List<Libro> lst = service.findByLikeCasaed(casaed);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
        }


        return resp;
    }

    @GetMapping("/by-categoria/{nome}")
    public ResponseEntity<List<Libro>> getByCategoria(@PathVariable("nome") String categoria, @RequestParam(value = "searchType", required = false) Integer searchType) {
        log.info("Invoked LibroController.getByAutore(" + categoria + ", " + searchType + ")");
        ResponseEntity<List<Libro>> resp = null;

        //default to like search
        if (searchType == null) searchType = 2;

        switch (searchType) {
            case 1:     //exact
                try {
                    List<Libro> lst = service.findByExactCategoria(categoria);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
            case 2:     //like
                try {
                    List<Libro> lst = service.findByLikeCategoria(categoria);
                    if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    else resp = new ResponseEntity<>(lst, HttpStatus.OK);
                } catch (Exception e) {
                    resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
        }
        return resp;
    }

    @Operation(description = "Inserisce un libro in db a partire da un JSON di Google Books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro inserito con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta malformata, verificare i parametri"),
            @ApiResponse(responseCode = "401", description = "Utente non loggato"),
            @ApiResponse(responseCode = "409", description = "Libro già presente"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    }
    )
    @PostMapping("/booksAPI")
    public ResponseEntity<Libro> insertFromGbooks(@RequestParam String data) {
        log.info("Invoked LibroController.insertFromGbooks(" + data + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();

        JSONObject json;
        try {
            json = new JSONObject(data);
            if (!json.has("volumeInfo")) return ResponseEntity.badRequest().build();
            Libro libro = Libro.fromJSON(json);

            Optional<Libro> lOpt = service.findByIsbn(libro.getIsbn());
            if (lOpt.isPresent()) return new ResponseEntity<>(HttpStatus.CONFLICT);

            Libro lSaved = service.save(libro);
            return ResponseEntity.ok(lSaved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @Operation(description = "Verifica la correttezza di una query, restituendo una o più possibilità")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, la lista delle possibilità è stata popolata"),
            @ApiResponse(responseCode = "401", description = "Sessione non settata e/o token invalido"),
            @ApiResponse(responseCode = "404", description = "Non sono stati ottenuti riscontri dalle API di Google Books"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping("/verify")
    public ResponseEntity<Object> verify(@RequestParam("q") String q) {
        log.info("Invoked AnnunciLibriController.verify(" + q + ")");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();

        try {
            Optional<Libro> libroOptional = service.findByIsbn(q);

            if(libroOptional.isPresent()) return ResponseEntity.ok(Collections.singletonList(libroOptional.get()));

            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + q;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JSONObject json = new JSONObject(response);
            if (json.getInt("totalItems") != 0) {
                JSONArray arr = new JSONArray();
                arr.put(json.getJSONArray("items").getJSONObject(0));
                return ResponseEntity.ok(arr.toString());
            }

            List<Libro> lst = service.findByLikeNome(q);
            lst.addAll(service.findByLikeIsbn(q));
            lst.addAll(service.findByLikeAutore(q));
            lst.addAll(service.findByLikeCasaed(q));
            lst.addAll(service.findByLikeCategoria(q));
            ObjectMapper om = new ObjectMapper();
            String jsonString = om.writeValueAsString(lst);

            url = "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(q, "UTF-8")+"&printType=books";
            response = restTemplate.getForObject(url, String.class);

            json = new JSONObject(response);
            if (json.getInt("totalItems") == 0) return ResponseEntity.notFound().build();
            JSONArray items = json.getJSONArray("items");
            JSONArray tmp = new JSONArray();
            for (int i = 0; i < Math.min(items.length(), 10); i++) {
                if(items.getJSONObject(i).getJSONObject("saleInfo").getBoolean("isEbook")) continue;
                tmp.put(items.getJSONObject(i));
            }

            JSONArray arr = new JSONArray(jsonString);
            for (int i = 0; i < arr.length(); i++) {
                tmp.put(arr.getJSONObject(i));
            }
            return ResponseEntity.ok(tmp.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Inserisce un libro in db a partire dal form di inserimento del sito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro inserito con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta malformata, verificare i parametri"),
            @ApiResponse(responseCode = "401", description = "Utente non loggato"),
            @ApiResponse(responseCode = "409", description = "Libro già presente"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @PostMapping
    public ResponseEntity<Object> insert(InsertLibroForm form) {
        log.info("Invoked LibroController.insert(" + form + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();

        Optional<Libro> lOpt = service.findByIsbn(form.getIsbn());
        if (lOpt.isPresent()) return new ResponseEntity<>(HttpStatus.CONFLICT);

        if(form.getIsbn() == null || form.getIsbn().isEmpty()) return ResponseEntity.badRequest().body("{\"invalidField\" : \"isbn\"}");
        for(char c : form.getIsbn().toCharArray()) {
            if(!Character.isDigit(c)) return ResponseEntity.badRequest().body("{\"invalidField\" : \"isbn\"}");
        }
        if(form.getPrezzo() != null && form.getPrezzo() <= 0) return ResponseEntity.badRequest().body("{\"invalidField\" : \"prezzo\"}");
        if(form.getNpag() != null && form.getNpag() <= 0) return ResponseEntity.badRequest().body("{\"invalidField\" : \"npag\"}");

        try {
            Libro l = Libro.fromForm(form);
            Libro lSaved = service.save(l);
            return new ResponseEntity<>(lSaved, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
