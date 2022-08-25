package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Notifica;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.NotificaRepository;
import it.leonardo.leonardoapiboot.service.NotificaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificaServiceImpl implements NotificaService {

    @Autowired
    private NotificaRepository notificaRepository;

    @Override
    public List<Notifica> findAllByUtente(Utente utente) {
        return notificaRepository.findAllByUtente(utente);
    }

    @Override
    public List<Notifica> findAllByUtenteAndLettoIsFalse(Utente utente) {
        return notificaRepository.findAllByUtenteAndLettoIsFalse(utente);
    }

    @Override
    public void setNotificheAsRead(Utente utente) {
        List<Notifica> notifiche = findAllByUtenteAndLettoIsFalse(utente);

        for (Notifica notifica : notifiche) {
            notifica.setLetto(true);
        }

        notificaRepository.saveAll(notifiche);
    }

    @Override
    public void setNotificheAsReadById(Integer id, Utente utente) {
        Notifica notifica = notificaRepository.findById(id).get();
        if (!Objects.equals(notifica.getUtente().getUtenteId(), utente.getUtenteId()))
            return;
        notifica.setLetto(true);
        notificaRepository.save(notifica);
    }

    @Override
    public Notifica save(Notifica notifica) {
        return notificaRepository.save(notifica);
    }

    @Override
    public List<Notifica> saveAll(List<Notifica> notifiche) {
        return notificaRepository.saveAll(notifiche);
    }
}

