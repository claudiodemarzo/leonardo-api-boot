package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;

public interface ChatroomService {
    Chatroom getOrCreate(Utente mit, Utente dest);

    List<Chatroom> getByUtenteMit(Utente mit);

    Chatroom save(Chatroom c);

    List<Chatroom> getByUtenteDest(Utente dest);
}
