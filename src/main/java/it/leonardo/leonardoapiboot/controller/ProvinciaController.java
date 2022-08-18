package it.leonardo.leonardoapiboot.controller;


import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(description = "Restituisce una lista di tutti le province disponibili sul database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine. La lista è popolata e restituita"),
            @ApiResponse(responseCode = "204", description = "Richiesta andata a buon fine, ma la lista è vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
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
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }

        return resp;
    }

    @GetMapping("{id}")
    @Operation(description = "Restituisce l'oggetto relativo alla provincia il quale id è fornito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine, l'oggetto è restituito"),
            @ApiResponse(responseCode = "404", description = "Non è stato possibile trovare l'oggetto associato all'id fornito"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
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
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }

        return resp;
    }

    @GetMapping("/by-regione/{id}")
    @Operation(description = "Restituisce una lista di tutti le province disponibili sul database, che si trovano all'interno della regione il quale id è fornito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine. La lista è popolata e restituita"),
            @ApiResponse(responseCode = "204", description = "Richiesta andata a buon fine, ma la lista è vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Object> getByRegione(@PathVariable("id") Integer id) {
        log.info("Invoked ProvinciaController.getByRegione(" + id + ")");
        ResponseEntity<Object> resp = null;
        Regione r = null;
        try {
            Optional<Regione> rOpt = regioneService.getById(id);
            if (rOpt.isPresent()) r = rOpt.get();
            else resp = new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
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
                Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
            }
        }
        return resp;
    }
}
