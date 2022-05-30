package it.leonardo.leonardoapiboot.controller;


import it.leonardo.leonardoapiboot.entity.Regione;
import it.leonardo.leonardoapiboot.service.RegioneService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/regione")
@CrossOrigin(origins = "*")
public class RegioneController {

    private final Log log = LogFactory.getLog(RegioneController.class);

    @Autowired
    private RegioneService service;

    @GetMapping
    public ResponseEntity<List<Regione>> getAllRegione() {
        log.info("Invoked TagController.getAllRegione()");
        ResponseEntity<List<Regione>> response;

        try {
            List<Regione> regioni = service.getAll();
            if (regioni.isEmpty()) {
                response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                response = new ResponseEntity<>(regioni, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("{id}")
    public ResponseEntity<Regione> getRegioneById(@PathVariable("id") Integer id) {
        log.info("Invoked TagController.getRegioneById(" + id + ")");
        ResponseEntity<Regione> response;

        try {
            Optional<Regione> regione = service.getById(id);
            if (regione.isPresent()) {
                response = new ResponseEntity<>(regione.get(), HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
