package it.leonardo.leonardoapiboot.controller;

import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.service.LibroService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/libri")
@CrossOrigin(origins = "*")
public class LibroController {

    private final Log log = LogFactory.getLog(LibroController.class);

    @Autowired
    private LibroService service;

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

    @PostMapping
    public ResponseEntity<Libro> insert() {
        log.info("Invoked LibroController.insert()");
        return null;
    }
}
