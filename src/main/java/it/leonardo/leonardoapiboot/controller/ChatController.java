package it.leonardo.leonardoapiboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Messaggio;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.service.ChatroomService;
import it.leonardo.leonardoapiboot.service.MessaggioService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private MessaggioService messaggioService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UtenteService utenteService;

    private final Log log = LogFactory.getLog(ChatController.class);


    @Operation(description = "Resitutisce una lista di tutti i messaggi che l'utente richiedente ha scambiato con l'utente specificato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messaggi trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "404", description = "Messaggi non trovati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<Messaggio>> getMessagesWithUtente(@PathVariable Integer id) {
        log.info("Invoked ChatController.getMessagesWithUtente(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent()) return ResponseEntity.notFound().build();
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            List<Messaggio> messaggi = new ArrayList<>();
            List<Messaggio> tmp = messaggioService.findByUtenteMitAndUtenteDest(mit, dest);
            if (!tmp.isEmpty()) messaggi.addAll(tmp);
            tmp = messaggioService.findByUtenteMitAndUtenteDest(dest, mit);
            if (!tmp.isEmpty()) messaggi.addAll(tmp);

            return ResponseEntity.ok(messaggi);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @Operation(description = "Restituisce il numero di messaggi non letti nella chat con un utente specificato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messaggi trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "404", description = "Messaggi non trovati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}/unread")
    public ResponseEntity<Integer> getUnreadMessagesWithUtente(@PathVariable Integer id) {
        log.info("Invoked ChatController.getUnreadMessagesWithUtente(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent()) return ResponseEntity.notFound().build();
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            Integer count = messaggioService.getUnreadMessagesCount(mit, dest);
            if(count == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Imposta i messaggi ricevuto dall'utente specificato come letti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messaggi trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "404", description = "Messaggi non trovati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}/read")
    public ResponseEntity<Object> setMessagesAsRead(@PathVariable Integer id) {
        log.info("Invoked ChatController.setMessagesAsRead(" + id + ")");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent()) return ResponseEntity.notFound().build();
            Utente dest = utenteService.findById(Integer.parseInt(userID)).get(), mit = otherUtenteOptional.get();
            messaggioService.setMessagesAsRead(mit, dest);
            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Restituisce tutte le chat nel quale l'utente è coinvolto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat trovate"),
            @ApiResponse(responseCode = "204", description = "Nessuna chat ancora aperta"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/open")
    public ResponseEntity<List<Chatroom>> getOpenChatrooms(){
        log.info("Invoked ChatController.getOpenChatrooms()");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            List<Chatroom> chatrooms = chatroomService.getByUtenteMit(utenteService.findById(Integer.parseInt(userID)).get());
            if(chatrooms.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(chatrooms);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
