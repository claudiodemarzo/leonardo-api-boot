package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
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
    @SentrySpan
    public List<Notifica> findAllByUtente(Utente utente) {
        return notificaRepository.findAllByUtente(utente);
    }

    @Override
    @SentrySpan
    public List<Notifica> findAllByUtenteAndCancellatoIsFalse(Utente utente){
        return notificaRepository.findAllByUtenteAndCancellatoIsFalse(utente);
    }

    @Override
    @SentrySpan
    public List<Notifica> findAllByUtenteAndLettoIsFalseAndCancellatoIsFalse(Utente utente) {
        return notificaRepository.findAllByUtenteAndLettoIsFalseAndCancellatoIsFalse(utente);
    }

    @Override
    @SentrySpan
    public void setNotificheAsRead(Utente utente) {
        List<Notifica> notifiche = findAllByUtenteAndLettoIsFalseAndCancellatoIsFalse(utente);

        for (Notifica notifica : notifiche) {
            notifica.setLetto(true);
        }

        notificaRepository.saveAll(notifiche);
    }

    @Override
    @SentrySpan
    public void setNotificheAsReadById(Integer id, Utente utente) {
        Notifica notifica = notificaRepository.findById(id).get();
        if (!Objects.equals(notifica.getUtente().getUtenteId(), utente.getUtenteId()))
            return;
        notifica.setLetto(true);
        notificaRepository.save(notifica);
    }

    @Override
    @SentrySpan
    public Notifica save(Notifica notifica) {
        return notificaRepository.save(notifica);
    }

    @Override
    @SentrySpan
    public void delete(Integer id, Utente utente) {
        Notifica notifica = notificaRepository.findById(id).get();
        if (!Objects.equals(notifica.getUtente().getUtenteId(), utente.getUtenteId()))
            return;
        notifica.setLetto(true);
        notifica.setCancellato(true);
        notificaRepository.save(notifica);
    }

    @Override
    @SentrySpan
    public List<Notifica> saveAll(List<Notifica> notifiche) {
        return notificaRepository.saveAll(notifiche);
    }

    @Override
    @SentrySpan
    public void deleteAll(Utente utente) {
        List<Notifica> notifiche = findAllByUtente(utente);
        for (Notifica notifica : notifiche) {
            notifica.setLetto(true);
            notifica.setCancellato(true);
        }
        notificaRepository.saveAll(notifiche);
    }
}

