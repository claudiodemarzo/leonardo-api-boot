package it.leonardo.leonardoapiboot.controller;


import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Livello;
import it.leonardo.leonardoapiboot.service.LivelloService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livelli")
@CrossOrigin(origins = "*")
public class LivelloController {

    private final Log log = LogFactory.getLog(LivelloController.class);

    @Autowired
    private LivelloService service;

    @GetMapping
    @Operation(description = "Restituisce una lista di tutti i livelli raggiungibili.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine. La lista è popolata e restituita"),
            @ApiResponse(responseCode = "204", description = "Richiesta andata a buon fine, ma la lista è vuota"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<List<Livello>> getAll() {
        log.info("Invoked LivelloController.getAll()");

        try {
            List<Livello> lst = service.getAll();
            if (lst.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(lst);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    @Operation(description = "Restituisce l'oggetto relativo al livello il quale id è fornito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine, l'oggetto è restituito"),
            @ApiResponse(responseCode = "404", description = "Non è stato possibile trovare l'oggetto associato all'id fornito"),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    public ResponseEntity<Livello> getById(@PathVariable("id") Integer id) {
        log.info("Invoked LivelloController.getById(" + id + ")");

        try {
            Optional<Livello> lvl = service.getById(id);
            if (!lvl.isPresent()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(lvl.get());
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }

    }
}
