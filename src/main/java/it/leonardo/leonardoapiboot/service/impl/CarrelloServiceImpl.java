package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Carrello;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.CarrelloRepository;
import it.leonardo.leonardoapiboot.service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrelloServiceImpl implements CarrelloService {

    @Autowired
    private CarrelloRepository repo;

    @Override
    @SentrySpan
    public List<Carrello> getByUtente(Utente u) {
        return repo.findAllByUtente(u);
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
    public void deleteAllByUtente(Utente u) {
        repo.deleteCarrelloByUtente(u);
    }
}
