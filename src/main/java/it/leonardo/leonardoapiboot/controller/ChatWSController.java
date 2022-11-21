package it.leonardo.leonardoapiboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.leonardo.leonardoapiboot.entity.*;
import it.leonardo.leonardoapiboot.entity.form.MessaggioWS;
import it.leonardo.leonardoapiboot.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ChatWSController {

    private static final Log log = LogFactory.getLog(ChatWSController.class);
    private static SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private MessaggioService messaggioService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

    @Autowired
    private RichiestaService richiestaService;
    @Autowired
    private AnnunciLibriService annunciLibriService;

    public ChatWSController(SimpMessagingTemplate messagingTemplate) {
        ChatWSController.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/broad")
    @SendTo("/topic/broadcast")
    public Messaggio broadcastNews(@Payload MessaggioWS message, Principal principal) {
        return null;
    }

    @Operation(description = "Invia un messaggio all'utente specificato")
    @MessageMapping("/private-message")
    public void privateMessage(@Payload MessaggioWS message, Principal principal) {
        log.info("Invoked ChatWSController.privateMessage()");
        Messaggio m = new Messaggio();
        Integer utenteMit = Integer.parseInt(principal.getName());
        Integer utenteDest = message.getUtenteDest();
        Utente mit = utenteService.findById(utenteMit).orElse(null);
        if (mit == null) return;
        if(!mit.getEmail_confermata()) {
            log.info("Utente non ha confermato l'email");
            ChatWSController.sendNotification(mit.getUtenteId().toString(), new Notifica(Notifica.TipoNotifica.internal, "", "{\"context\" : \"userNotVerified\", \"value\" : true}", null, null), null);
            return;
        }
        Utente dest = utenteService.findById(utenteDest).orElse(null);
        if (dest == null) return;
        if (mit.getUtenteId().equals(dest.getUtenteId())) return;
        Chatroom c = chatroomService.getOrCreate(mit, dest);
        c.setUtenteMitInfo(utentePublicInfoService.getById(mit.getUtenteId()).get().getId());
        c.setUtenteDestInfo(utentePublicInfoService.getById(dest.getUtenteId()).get().getId());

        m.setChatroom(c);
        m.setStatus(0);
        m.setTimestamp(Date.from(Instant.now()));
        m.setMessaggio(message.getMessaggio());
        m.setTipo(message.getTipo());

        m = messaggioService.save(m);

        String msg = message.getMessaggio();
        Matcher matcher = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE).matcher(msg);
        for (int i = 0; matcher.find(); i++) {
            String url = matcher.group();
            String url2 = url.substring(1, url.length() - 1);
            msg = msg.replace(url, "<a href=\"" + url2 + "\" target=\"_blank\">" + url2 + "</a>");
        }

        m = messaggioService.findById(m.getMessaggioId()).get();

        m.setMessaggio(msg);

        messagingTemplate.convertAndSendToUser(utenteDest.toString(), "/topic/private-message", m.toString());
    }

    @Operation(description = "Notifica l'utente fornito con un messaggio specificato")
    public static void sendNotification(String userId, Notifica notifica, NotificaService notificaService) {
        log.info("Invoked ChatWSController.sendNotification()");
        notifica.setTimestamp(Date.from(Instant.now()));
        if (notifica.getType() != Notifica.TipoNotifica.internal) notifica = notificaService.save(notifica);
        messagingTemplate.convertAndSendToUser(userId, "/topic/notification", notifica.toString());
    }

    @Operation(description = "Invia un messaggio all'utente fornito")
    public static void sendMessage(Utente sender, Utente recipient, String messaggioStr, String tipoMessaggio, ChatroomService chatroomService, UtentePublicInfoService utentePublicInfoService, MessaggioService messaggioService, AnnunciLibriService annunciLibriService, RichiestaService richiestaService) {
        log.info("Invoked ChatWSController.sendMessage()");
        Messaggio messaggio = new Messaggio();
        Integer utenteMit = sender.getUtenteId();
        Integer utenteDest = recipient.getUtenteId();
        if (sender.getUtenteId().equals(recipient.getUtenteId())) return;
        Chatroom c = chatroomService.getOrCreate(sender, recipient);
        c.setUtenteMitInfo(utentePublicInfoService.getById(sender.getUtenteId()).get().getId());
        c.setUtenteDestInfo(utentePublicInfoService.getById(recipient.getUtenteId()).get().getId());

        messaggio.setChatroom(c);
        messaggio.setStatus(0);
        messaggio.setTimestamp(Date.from(Instant.now()));
        messaggio.setMessaggio(messaggioStr);
        messaggio.setTipo(tipoMessaggio);

        messaggio = messaggioService.save(messaggio);

        if(tipoMessaggio.equalsIgnoreCase("request")) {
            JSONObject json = new JSONObject(messaggioStr);
            Richiesta r = new Richiesta();
            r.setMessaggio(messaggio);
            r.setAnnuncio(annunciLibriService.findById(json.getInt("annuncio")).get());
            r.setStato(null);
            r = richiestaService.save(r);
            messaggio.setRichiesta(r);
        }

        messagingTemplate.convertAndSendToUser(recipient.getUtenteId().toString(), "/topic/private-message", messaggio.toString());
    }
}
