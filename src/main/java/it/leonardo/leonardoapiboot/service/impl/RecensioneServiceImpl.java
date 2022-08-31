package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Recensione;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.RecensioneRepository;
import it.leonardo.leonardoapiboot.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecensioneServiceImpl implements RecensioneService {

    @Autowired
    private RecensioneRepository repo;

    @Override
    @SentrySpan
    public List<Recensione> getByUtenteRecensore(Utente u) {

        return repo.findAllByUtenteRecensore(u);
    }

    @Override
    @SentrySpan
    public List<Recensione> getByUtenteRecensito(Utente u) {

        return repo.findAllByUtenteRecensito(u);
    }

    @Override
    @SentrySpan
    public Optional<Recensione> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public Recensione save(Recensione r) {
        return repo.save(r);
    }
}
