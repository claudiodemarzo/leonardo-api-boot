package it.leonardo.leonardoapiboot.controller;


import io.sentry.Sentry;
import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.CittaSimple;
import it.leonardo.leonardoapiboot.entity.Provincia;
import it.leonardo.leonardoapiboot.entity.Regione;
import it.leonardo.leonardoapiboot.service.CittaService;
import it.leonardo.leonardoapiboot.service.CittaSimpleService;
import it.leonardo.leonardoapiboot.service.ProvinciaService;
import it.leonardo.leonardoapiboot.service.RegioneService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/citta")
@CrossOrigin(origins = "*")
public class CittaController {

    private final Log log = LogFactory.getLog(CittaController.class);

    @Autowired
    private CittaService service;

    @Autowired
    private ProvinciaService provServ;

    @Autowired
    private RegioneService regServ;

    @Autowired
    private CittaSimpleService cittaSimpleService;

    @GetMapping
    public ResponseEntity<List> getAll(@RequestParam(required = false, defaultValue = "true") Boolean details) {
        log.info("Invoked CittaController.getAll(" + details + ")");

        try {
            if (details) {
                List<Citta> list = service.getAll();
                if (list.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(list);
            }

            List<CittaSimple> list = cittaSimpleService.findAll();
            if (list.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Integer id, @RequestParam(required = false, defaultValue = "true") Boolean details) {
        log.info("Invoked CittaController.getById(" + id + ", " + details + ")");

        try {
            if (details) {
                Optional<Citta> citta = service.getById(id);
                if (!citta.isPresent()) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(citta.get());
            }

            Optional<CittaSimple> citta = cittaSimpleService.findAllById(id);
            if (!citta.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(citta.get());

        } catch (Exception e) {
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/by-provincia/{id}")
    public ResponseEntity<Object> getByProvincia(@PathVariable("id") Integer id) {
        log.info("Invoked CittaController.getByProvincia(" + id + ")");
        ResponseEntity<Object> resp = null;
        Provincia prov = null;

        try {
            Optional<Provincia> provOpt = provServ.getById(id);
            if (provOpt.isPresent()) prov = provOpt.get();
            else resp = new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Sentry.captureException(e);
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (prov != null) {
            List<Citta> lst = service.getByProvincia(prov);
            if (lst.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else resp = new ResponseEntity<>(lst, HttpStatus.OK);
        }


        return resp;
    }

    @GetMapping("/by-regione/{id}")
    public ResponseEntity<Object> getByRegione(@PathVariable("id") Integer id) {
        log.info("Invoked CittaController.getByRegione(" + id + ")");
        ResponseEntity<Object> resp = null;
        Regione reg = null;
        List<Provincia> lstProv = null;
        List<Citta> resLst = new ArrayList<>();

        try {
            Optional<Regione> regOpt = regServ.getById(id);
            if (regOpt.isPresent()) {
                reg = regOpt.get();
            } else {
                resp = new ResponseEntity<>("{\"invalidField\":\"id\"}", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Sentry.captureException(e);
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (reg != null) {
            lstProv = provServ.getByRegione(reg);
            if (lstProv.isEmpty()) {
                resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                lstProv.forEach((p) -> {
                    List<Citta> tmp = service.getByProvincia(p);
                    resLst.addAll(tmp);
                });
                resp = new ResponseEntity<>(resLst, HttpStatus.OK);
            }
        }

        return resp;
    }
}
