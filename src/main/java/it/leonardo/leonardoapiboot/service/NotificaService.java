package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Notifica;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;

public interface NotificaService {

    List<Notifica> findAllByUtente(Utente utente);

    List<Notifica> findAllByUtenteAndLettoIsFalse(Utente utente);

    void setNotificheAsRead(Utente utente);

    void setNotificheAsReadById(Integer id, Utente utente);

    Notifica save(Notifica notifica);

    List<Notifica> saveAll(List<Notifica> notifiche);
}
