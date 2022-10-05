package it.leonardo.leonardoapiboot.controller;

import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Messaggio;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.form.MessaggioWS;
import it.leonardo.leonardoapiboot.service.ChatroomService;
import it.leonardo.leonardoapiboot.service.MessaggioService;
import it.leonardo.leonardoapiboot.service.UtentePublicInfoService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import it.leonardo.leonardoapiboot.utils.ChatroomComparator;
import it.leonardo.leonardoapiboot.utils.MessaggiComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

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
            for (Messaggio m : messaggi) {
                m.getChatroom().setUtenteMitInfo(m.getChatroom().getUtenteMit().getUtenteId());
            }

            messaggi.sort(new MessaggiComparator());

            return ResponseEntity.ok(messaggi);
        } catch (Exception e) {
            Sentry.captureException(e);
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
            if (count == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            Sentry.captureException(e);
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

            Chatroom chatroom = chatroomService.getOrCreate(mit, dest);
            chatroom.setMarkedUnread(false);
            chatroomService.save(chatroom);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
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
    public ResponseEntity<List<Chatroom>> getOpenChatrooms() {
        log.info("Invoked ChatController.getOpenChatrooms()");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            List<Chatroom> chatrooms = chatroomService.getByUtenteDest(utenteService.findById(Integer.parseInt(userID)).get());
            if (chatrooms.isEmpty()) return ResponseEntity.noContent().build();
            Breadcrumb bBeforeSorting = new Breadcrumb(),
                    bAfterSortingMessages = new Breadcrumb(),
                    bAfterSortingChatroomsPerMessages = new Breadcrumb();

            bBeforeSorting.setData("chatrooms", chatrooms);
            bBeforeSorting.setLevel(SentryLevel.DEBUG);
            bBeforeSorting.setMessage("Chatrooms prima di ordinamento");

            List<Messaggio> messaggi;

            for (Chatroom c : chatrooms) {
                messaggi = new ArrayList<>();
                Chatroom reverse = chatroomService.getOrCreate(c.getUtenteDest(), c.getUtenteMit());

                messaggi.addAll(reverse.getMessaggi());
                messaggi.addAll(c.getMessaggi());

                messaggi.sort(new MessaggiComparator());
                Collections.reverse(messaggi);
                c.getMessaggi().sort(new MessaggiComparator());
                c.setLastMessageDate(messaggi.size() != 0 ? messaggi.get(0).getTimestamp() : null);
            }
            bAfterSortingMessages.setData("chatrooms", chatrooms);
            bAfterSortingMessages.setLevel(SentryLevel.DEBUG);
            bAfterSortingMessages.setMessage("Chatrooms dopo ordinamento di Messaggi");

            for (Chatroom c : chatrooms)
                c.setUtenteMitInfo(utentePublicInfoService.getById(c.getUtenteMit().getUtenteId()).get());

            //chatrooms.forEach(c -> c.getMessaggi().forEach(log::warn));
            for (Chatroom c : chatrooms)
                c.setUnreadMessages(messaggioService.getUnreadMessagesCount(chatroomService.getOrCreate(c.getUtenteMit(), c.getUtenteDest()).getChatroomId()));

            chatrooms.sort(new ChatroomComparator());

            bAfterSortingChatroomsPerMessages.setData("chatrooms", chatrooms);
            bAfterSortingChatroomsPerMessages.setLevel(SentryLevel.DEBUG);
            bAfterSortingChatroomsPerMessages.setMessage("Chatrooms dopo ordinamento");

            Sentry.addBreadcrumb(bBeforeSorting);
            Sentry.addBreadcrumb(bAfterSortingMessages);
            Sentry.addBreadcrumb(bAfterSortingChatroomsPerMessages);

            return ResponseEntity.ok(chatrooms);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(description = "Imposta una chat come non letta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat impostata come non letta"),
            @ApiResponse(responseCode = "404", description = "Chat/Utente non trovati"),
            @ApiResponse(responseCode = "401", description = "Login non effettuato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping("/{id}/mark-unread")
    public ResponseEntity<Object> setUnread(@PathVariable Integer id) {
        log.info("Invoked ChatController.setUnread()");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(id);
            if (!otherUtenteOptional.isPresent())
                return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            Chatroom chatroom = chatroomService.getOrCreate(dest, mit);

            chatroom.setMarkedUnread(true);

            chatroomService.save(chatroom);

            return ResponseEntity.ok("{}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/sendImage")
    public ResponseEntity<Object> sendImage(String id, MultipartFile file, String tipo){
        log.info("Invoked ChatController.sendImage()");
        String token = session.getAttribute("token") == null ? null : session.getAttribute("token").toString();

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userID = session.getAttribute("userID").toString();
        try {
            Optional<Utente> otherUtenteOptional = utenteService.findById(Integer.parseInt(id));
            if (!otherUtenteOptional.isPresent())
                return new ResponseEntity<>("{\"invalidField\" : \"id\"}", HttpStatus.NOT_FOUND);
            Utente dest = otherUtenteOptional.get(), mit = utenteService.findById(Integer.parseInt(userID)).get();
            Chatroom chatroom = chatroomService.getOrCreate(dest, mit);

            Instant now = Instant.now();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

            String fileName = chatroom.getChatroomId()+"/" + sdf.format(Date.from(now)) + file.getOriginalFilename().split(".")[file.getOriginalFilename().split(".").length-1];

            file.transferTo(new File("/var/www/html/assets/imgs/chat/"+fileName));

            Messaggio messaggio = new Messaggio();
            messaggio.setChatroom(chatroom);
            messaggio.setTimestamp(Date.from(now));
            messaggio.setMessaggio(fileName);
            messaggio.setTipo(String.valueOf(switch (tipo) {
                case "image" -> MessaggioWS.TipoMessaggio.IMAGE;
                case "location" -> MessaggioWS.TipoMessaggio.LOCATION;
                default -> MessaggioWS.TipoMessaggio.IMAGE;
            }));

            ChatWSController.sendMessage(chatroom.getUtenteMit(), chatroom.getUtenteDest(), fileName, MessaggioWS.TipoMessaggio.IMAGE ,chatroomService, utentePublicInfoService, messaggioService);

            messaggioService.save(messaggio);
            return ResponseEntity.ok("{fileName: \""+fileName+"\"}");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ResponseEntity.internalServerError().build();
        }
    }


}
