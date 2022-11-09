package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
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
    public Optional<Messaggio> findById(Integer id) {
        return messaggioRepository.findById(id);
    }

    @Override
    @SentrySpan
    public List<Messaggio> findByUtenteMitAndUtenteDest(Utente mit, Utente dest) {
        Optional<Chatroom> chatroomOpt = chatroomRepository.findByUtenteMitAndUtenteDest(mit, dest);
        if (!chatroomOpt.isPresent()) return null;
        Chatroom chatroom = chatroomOpt.get();
        return messaggioRepository.findByChatroom(chatroom);
    }

    @Override
    @SentrySpan
    public Integer getUnreadMessagesCount(Utente mit, Utente dest) {
        Optional<Chatroom> chatroomOpt = chatroomRepository.findByUtenteMitAndUtenteDest(dest, mit);
        if (!chatroomOpt.isPresent()) return null;

        return getUnreadMessagesCount(chatroomOpt.get().getChatroomId());
    }

    @Override
    @SentrySpan
    public Integer getUnreadMessagesCount(Integer c) {
        return Math.toIntExact(chatroomRepository.findById(c).get().getMessaggi().stream().filter(m -> m.getStatus() == 0).count());
    }

    @Override
    @SentrySpan
    public void setMessagesAsRead(Utente mit, Utente dest) {
        Optional<Chatroom> chatroomOpt = chatroomRepository.findByUtenteMitAndUtenteDest(mit, dest);
        if (!chatroomOpt.isPresent()) return;

        Chatroom chatroom = chatroomOpt.get();
        List<Messaggio> messaggi = messaggioRepository.findByChatroom(chatroom);
        messaggi.forEach(m -> m.setStatus(1));
        messaggioRepository.saveAll(messaggi);
    }

    @Override
    @SentrySpan
    public Messaggio save(Messaggio m) {
        return messaggioRepository.save(m);
    }
}
