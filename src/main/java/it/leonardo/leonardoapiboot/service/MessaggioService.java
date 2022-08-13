package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Messaggio;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;

public interface MessaggioService {

    List<Messaggio> findByUtenteMitAndUtenteDest(Utente mit, Utente dest);

    Integer getUnreadMessagesCount(Utente mit, Utente dest);

    Integer getUnreadMessagesCount(Integer c);

    void setMessagesAsRead(Utente mit, Utente dest);

    Messaggio save(Messaggio m);
}
