package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Notifica;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;

public interface NotificaService {

    List<Notifica> findAllByUtente(Utente utente);

    List<Notifica> findAllByUtenteAndCancellatoIsFalse(Utente utente);

    List<Notifica> findAllByUtenteAndLettoIsFalseAndCancellatoIsFalse(Utente utente);

    void setNotificheAsRead(Utente utente);

    void setNotificheAsReadById(Integer id, Utente utente);

    Notifica save(Notifica notifica);

    void delete(Integer id, Utente utente);

    List<Notifica> saveAll(List<Notifica> notifiche);

    void deleteAll(Utente utente);
}
