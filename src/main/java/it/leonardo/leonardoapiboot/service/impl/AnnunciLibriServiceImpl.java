package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.AnnunciLibriRepository;
import it.leonardo.leonardoapiboot.service.AnnunciLibriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnunciLibriServiceImpl implements AnnunciLibriService {

    @Autowired
    private AnnunciLibriRepository repo;

    @Override
    @SentrySpan
    public List<AnnunciLibri> getAll() {
        return repo.findAll();
    }

    @Override
    @SentrySpan
    public Optional<AnnunciLibri> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public List<AnnunciLibri> getByUtente(Utente u) {
        return repo.findAllByUtente(u);
    }

    @Override
    @SentrySpan
    public List<AnnunciLibri> getByLibro(Libro l) {
        return repo.findAllByLibro(l);
    }

    @Override
    @SentrySpan
    public AnnunciLibri save(AnnunciLibri al) {
        return repo.save(al);
    }
}
