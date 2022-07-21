package it.leonardo.leonardoapiboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Messaggio;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.form.MessaggioWS;
import it.leonardo.leonardoapiboot.service.ChatroomService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.Date;

@Controller
public class ChatWSController {

    private final Log log = LogFactory.getLog(ChatWSController.class);
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private UtenteService utenteService;

    public ChatWSController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/broad")
    @SendTo("/topic/broadcast")
    public Messaggio broadcastNews(@Payload MessaggioWS message, Principal principal){
        return null;
    }

    @Operation(description = "Invia un messaggio all'utente specificato")
    @MessageMapping("/private-message")
    public void privateMessage(@Payload MessaggioWS message, Principal principal){
        log.info("Invoked ChatWSController.privateMessage()");
        Messaggio m = new Messaggio();
        Integer utenteMit = Integer.parseInt(principal.getName());
        Integer utenteDest = message.getUtenteDest();
        Utente mit = utenteService.findById(utenteMit).orElse(null);
        if(mit == null) return;
        Utente dest = utenteService.findById(utenteDest).orElse(null);
        if(dest == null) return;
        Chatroom c = chatroomService.getOrCreate(mit, dest);

        m.setChatroom(c);
        m.setStatus(false);
        m.setTimestamp(Date.from(Instant.now()));
        m.setMessaggio(message.getMessaggio());

        messagingTemplate.convertAndSendToUser(utenteDest.toString(), "/topic/private-message", m);
    }
}