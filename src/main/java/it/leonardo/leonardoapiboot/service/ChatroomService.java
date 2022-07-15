package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Utente;

public interface ChatroomService {
    Chatroom getOrCreate(Utente mit, Utente dest);
}
