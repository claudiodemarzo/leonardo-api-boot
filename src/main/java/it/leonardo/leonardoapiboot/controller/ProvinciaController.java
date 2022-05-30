package it.leonardo.leonardoapiboot.controller;


import it.leonardo.leonardoapiboot.entity.Provincia;
import it.leonardo.leonardoapiboot.entity.Regione;
import it.leonardo.leonardoapiboot.service.ProvinciaService;
import it.leonardo.leonardoapiboot.service.RegioneService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/provincia")
@CrossOrigin(origins = "*")
public class ProvinciaController {

    private final Log log = LogFactory.getLog(ProvinciaController.class);

    @Autowired
    private ProvinciaService service;

    @Autowired
    private RegioneService regioneService;

    @GetMapping
    public ResponseEntity<List<Provincia>> getAll() {
        log.info("Invoked ProvinciaController.getAll()");
        ResponseEntity<List<Provincia>> resp = null;

        try {
            List<Provincia> list = service.getAll();
            if (list.isEmpty()) {
                resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                resp = new ResponseEntity<>(list, HttpStatus.OK);
            }
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @GetMapping("{id}")
    public ResponseEntity<Provincia> getById(@PathVariable("id") Integer id) {
        log.info("Invoked ProvinciaController.getById(" + id + ")");
        ResponseEntity<Provincia> resp = null;

        try {
            Optional<Provincia> prov = service.getById(id);
            if (prov.isPresent()) {
                resp = new ResponseEntity<>(prov.get(), HttpStatus.OK);
            } else {
                resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @GetMapping("/by-regione/{id}")
    public ResponseEntity<Object> getByRegione(@PathVariable("id") Integer id) {
        log.info("Invoked ProvinciaController.getByRegione(" + id + ")");
        ResponseEntity<Object> resp = null;
        Regione r = null;
        try {
            Optional<Regione> rOpt = regioneService.getById(id);
            if (rOpt.isPresent()) r = rOpt.get();
            else resp = new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (r != null) {
            try {
                List<Provincia> list = service.getByRegione(r);
                if (list.isEmpty()) {
                    resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    resp = new ResponseEntity<>(list, HttpStatus.OK);
                }
            } catch (Exception e) {
                resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return resp;
    }
}
