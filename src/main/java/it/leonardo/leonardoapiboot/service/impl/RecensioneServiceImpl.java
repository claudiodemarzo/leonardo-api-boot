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
        List<Recensione> lst = repo.findAllByUtenteRecensore(u);
        for (Recensione r : lst) {
            r.getUtenteRecensore().setAnnunciLibri(null);
            r.getUtenteRecensito().setAnnunciLibri(null);
        }
        return lst;
    }

    @Override
    @SentrySpan
    public List<Recensione> getByUtenteRecensito(Utente u) {
        List<Recensione> lst = repo.findAllByUtenteRecensito(u);
        for (Recensione r : lst) {
            r.getUtenteRecensito().setAnnunciLibri(null);
            r.getUtenteRecensore().setAnnunciLibri(null);
        }
        return lst;
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
