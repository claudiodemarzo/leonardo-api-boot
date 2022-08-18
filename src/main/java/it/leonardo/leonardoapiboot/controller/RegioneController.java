package it.leonardo.leonardoapiboot.controller;


import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Regione;
import it.leonardo.leonardoapiboot.service.RegioneService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/regione")
@CrossOrigin(origins = {"https://leonardostart.tk", "https://localhost"}, allowCredentials = "true")
public class RegioneController {

    private final Log log = LogFactory.getLog(RegioneController.class);

    @Autowired
    private RegioneService service;

    @GetMapping
    @Operation(description = "Ottiene una lista di tutte le Regioni presenti nel Database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La lista è stata popolata e viene restituita"),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista risultante è vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<List<Regione>> getAllRegione() {
        log.info("Invoked TagController.getAllRegione()");

        try {
            List<Regione> regioni = service.getAll();

            if (regioni.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(regioni);
        } catch (Exception e) {
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    @Operation(description = "Ottiene una Regione il quale id è fornito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine, l'oggetto è restituito"),
            @ApiResponse(responseCode = "404", description = "Richiesta andata a buon fine, ma l'oggetto non è stato trovato"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Regione> getRegioneById(@PathVariable("id") Integer id) {
        log.info("Invoked TagController.getRegioneById(" + id + ")");

        try {
            Optional<Regione> regione = service.getById(id);

            if (!regione.isPresent()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(regione.get());
        } catch (Exception e) {
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }
    }
}
