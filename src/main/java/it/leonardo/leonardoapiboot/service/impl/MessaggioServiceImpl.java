package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Messaggio;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.ChatroomRepository;
import it.leonardo.leonardoapiboot.repository.MessaggioRepository;
import it.leonardo.leonardoapiboot.service.MessaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessaggioServiceImpl implements MessaggioService {

    @Autowired
    private MessaggioRepository messaggioRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Override
    public List<Messaggio> findByUtenteMitAndUtenteDest(Utente mit, Utente dest) {
        Optional<Chatroom> chatroomOpt = chatroomRepository.findByUtenteMitAndUtenteDest(mit, dest);
        if (!chatroomOpt.isPresent()) return null;
        Chatroom chatroom = chatroomOpt.get();
        return messaggioRepository.findByChatroom(chatroom);
    }

    @Override
    public Integer getUnreadMessagesCount(Utente mit, Utente dest) {
        Optional<Chatroom> chatroomOpt = chatroomRepository.findByUtenteMitAndUtenteDest(dest, mit);
        if (!chatroomOpt.isPresent()) return null;

        Chatroom chatroom = chatroomOpt.get();
        return Math.toIntExact(chatroom.getMessaggi().stream().filter(m -> !m.getStatus()).count());
    }

    @Override
    public void setMessagesAsRead(Utente mit, Utente dest) {
        Optional<Chatroom> chatroomOpt = chatroomRepository.findByUtenteMitAndUtenteDest(mit, dest);
        if (!chatroomOpt.isPresent()) return;

        Chatroom chatroom = chatroomOpt.get();
        List<Messaggio> messaggi = messaggioRepository.findByChatroom(chatroom);
        messaggi.forEach(m -> m.setStatus(true));
        messaggioRepository.saveAll(messaggi);
    }

    @Override
    public Messaggio save(Messaggio m) {
        return messaggioRepository.save(m);
    }
}