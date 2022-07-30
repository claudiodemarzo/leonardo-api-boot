package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.ChatroomRepository;
import it.leonardo.leonardoapiboot.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatroomServiceImpl implements ChatroomService {

    @Autowired
    private ChatroomRepository repo;

    @Override
    public Chatroom getOrCreate(Utente mit, Utente dest) {
        Optional<Chatroom> chatroomOpt = repo.findByUtenteMitAndUtenteDest(mit, dest);
        if(chatroomOpt.isPresent()) return chatroomOpt.get();
        Chatroom c1 = new Chatroom(), c2 = new Chatroom();
        c1.setUtenteDest(dest);
        c1.setUtenteMit(mit);
        c2.setUtenteDest(mit);
        c2.setUtenteMit(dest);
        Chatroom c1Saved = repo.save(c1);
        repo.save(c2);
        return c1Saved;
    }

    @Override
    public List<Chatroom> getByUtenteMit(Utente mit) {
        return repo.findAllByUtenteMit(mit);
    }

    @Override
    public Chatroom save(Chatroom c) {
        return repo.save(c);
    }
}

