package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.controller.ChatWSController;
import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Carrello;
import it.leonardo.leonardoapiboot.entity.Notifica;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.CarrelloRepository;
import it.leonardo.leonardoapiboot.service.CarrelloService;
import it.leonardo.leonardoapiboot.service.NotificaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrelloServiceImpl implements CarrelloService {

    @Autowired
    private CarrelloRepository repo;

    @Autowired
    private NotificaService notificaService;

    @Override
    @SentrySpan
    public List<Carrello> getByUtente(Utente u) {
        return repo.findAllByUtente(u);
    }

    @Override
    @SentrySpan
    public List<Carrello> getByAnnuncio(AnnunciLibri ann) {
        return repo.findAllByAnnuncio(ann);
    }

    @Override
    @SentrySpan
    public Carrello save(Carrello c) {
        return repo.save(c);
    }

    @Override
    @SentrySpan
    public void deleteById(Integer id) {
        repo.deleteCarrelloByEntryId(id);
    }

    @Override
    @SentrySpan
    public void deleteByAnnuncio(AnnunciLibri ann) {
        List<Carrello> entries = repo.findAllByAnnuncio(ann);

        for (Carrello c : entries) {
            ChatWSController.sendNotification(c.getUtente().getUtenteId().toString(), new Notifica(Notifica.TipoNotifica.warning, "Annuncio non più disponibile", "L'annuncio al quale eri interessato, " + ann.getLibro().getNome() + ", di @" + ann.getUtente().getUsername() + " non è più disponibile.", c.getUtente()), notificaService);
            repo.deleteById(c.getEntryId());
        }
    }

    @Override
    @SentrySpan
    public void deleteAllByUtente(Utente u) {
        repo.deleteCarrelloByUtente(u);
    }
}
