package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Messaggio;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;
import java.util.Optional;

public interface MessaggioService {

    Optional<Messaggio> findById(Integer id);

    List<Messaggio> findByUtenteMitAndUtenteDest(Utente mit, Utente dest);

    Integer getUnreadMessagesCount(Utente mit, Utente dest);

    Integer getUnreadMessagesCount(Integer c);

    void setMessagesAsRead(Utente mit, Utente dest);

    Messaggio save(Messaggio m);
}
