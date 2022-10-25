package it.leonardo.leonardoapiboot.controller;

import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Notifica;
import it.leonardo.leonardoapiboot.service.NotificaService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = {"https://leonardostart.tk", "https://localhost", "https://buybooks.it"}, allowCredentials = "true")
public class NotificaController {

    private final Log log = LogFactory.getLog(NotificaController.class);

    @Autowired
    private NotificaService service;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private HttpSession session;

    @Operation(description = "Ottieni tutte le notifiche dell'utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, nel body è presente la lista popolata."),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è vuota"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping
    public ResponseEntity<List<Notifica>> getAll() {
        log.info("Invoked NotificaController.getAll()");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            List<Notifica> notifiche = service.findAllByUtenteAndCancellatoIsFalse(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            if (notifiche.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(notifiche);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Ottieni tutte le notifiche non lette dell'utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine, nel body è presente la lista popolata."),
            @ApiResponse(responseCode = "204", description = "La richiesta è andata a buon fine, ma la lista è vuota"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/unread")
    public ResponseEntity<List<Notifica>> getUnread() {
        log.info("Invoked NotificaController.getAll()");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            List<Notifica> notifiche = service.findAllByUtenteAndLettoIsFalseAndCancellatoIsFalse(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            if (notifiche.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(notifiche);
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Aggiorna le notifiche come lette")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @PostMapping("/read")
    public ResponseEntity<Object> read() {
        log.info("Invoked NotificaController.read()");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.setNotificheAsRead(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Aggiorna una notifica come letta, dato un id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @PostMapping("/read/{id}")
    public ResponseEntity<Object> readSingle(@PathVariable Integer id) {
        log.info("Invoked NotificaController.read(" + id + ")");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.setNotificheAsReadById(id, utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Aggiorna una notifica come cancellata, dato un id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
        log.info("Invoked NotificaController.delete(" + id + ")");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.delete(id, utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Aggiorna tutte le notifiche come cancellate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La richiesta è andata a buon fine"),
            @ApiResponse(responseCode = "403", description = "Utente non connesso"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @DeleteMapping("/")
    public ResponseEntity<Object> deleteAll() {
        log.info("Invoked NotificaController.deleteAll()");

        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            service.deleteAll(utenteService.findById(Integer.parseInt(session.getAttribute("userID").toString())).get());
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
