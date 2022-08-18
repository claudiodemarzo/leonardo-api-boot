package it.leonardo.leonardoapiboot.controller;

import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.service.TagService;
import it.leonardo.leonardoapiboot.service.TagsUtenteService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "*")
public class TagController {

    private final Log log = LogFactory.getLog(TagController.class);

    @Autowired
    private TagService service;

    @Autowired
    private TagsUtenteService tagsUtenteService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private HttpSession session;

    @Operation(description = "Restituisce tutti i tag presenti nel database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine, l'array è restituito"),
            @ApiResponse(responseCode = "204", description = "Richiesta ok, lista vuota"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Tag>> getAll() {
        log.info("Invoked TagController.getAll()");
        ResponseEntity<List<Tag>> resp = null;

        try {
            List<Tag> tags = service.getAll();
            if (tags.isEmpty()) resp = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else resp = new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            resp = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resp;
    }

    @Operation(description = "Restituisce il tag con l'id specificato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine"),
            @ApiResponse(responseCode = "404", description = "Tag non trovato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") Integer id) {
        log.info("Invoked TagController.getById(" + id + ")");

        try {
            Optional<Tag> tag = service.getById(id);
            if (!tag.isPresent()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(tag.get());
        } catch (Exception e) {
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }

    }


    @Operation(description = "Restituisce i tag dell'utente loggato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine, l'array è restituito"),
            @ApiResponse(responseCode = "204", description = "Richiesta ok, lista vuota"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @GetMapping
    public ResponseEntity<List<TagsUtente>> getTagsByLoggedUser() {
        log.info("Invoked TagController.getTagsByLoggedUser()");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();
        try {
            List<TagsUtente> tagsUtente = tagsUtenteService.getTagsUtenteByUtente(utenteService.findById(Integer.parseInt(userid)).get());
            if (tagsUtente.isEmpty()) return ResponseEntity.noContent().build();
            else return ResponseEntity.ok(tagsUtente);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Imposta il tag attivo per l'utente loggato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Richiesta andata a buon fine"),
            @ApiResponse(responseCode = "404", description = "Tag non trovato"),
            @ApiResponse(responseCode = "401", description = "Il token di sessione non è settato, di conseguenza non è possibile accedere a questo endpoint."),
            @ApiResponse(responseCode = "500", description = "Errore generico del server")
    })
    @PostMapping("/setactive/{id}")
    public ResponseEntity<Object> setActiveTag(@PathVariable Integer id) {
        log.info("Invoked TagController.setActiveTag(" + id + ")");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();
        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userid = session.getAttribute("userID").toString();
        try {
            Optional<Tag> tagOpt = service.getById(id);
            if(!tagOpt.isPresent()) return ResponseEntity.notFound().build();

            List<TagsUtente> tagsUtente = tagsUtenteService.getTagsUtenteByUtente(utenteService.findById(Integer.parseInt(userid)).get());
            tagsUtente.forEach(t -> t.setActive(t.getTag().getTagId().equals(id)));

            tagsUtenteService.saveAll(tagsUtente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
return ResponseEntity.internalServerError().build();
        }
    }

}
