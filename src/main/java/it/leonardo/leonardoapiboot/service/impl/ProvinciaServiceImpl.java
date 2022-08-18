package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Provincia;
import it.leonardo.leonardoapiboot.entity.Regione;
import it.leonardo.leonardoapiboot.repository.ProvinciaRepository;
import it.leonardo.leonardoapiboot.service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaServiceImpl implements ProvinciaService {

    @Autowired
    private ProvinciaRepository repo;

    @Override
    @SentrySpan
    public List<Provincia> getAll() {
        return repo.findAll();
    }

    @Override
    @SentrySpan
    public Optional<Provincia> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public List<Provincia> getByRegione(Regione r) {
        return repo.getProvinciaByRegione(r);
    }


}
