package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Regione;
import it.leonardo.leonardoapiboot.repository.RegioneRepository;
import it.leonardo.leonardoapiboot.service.RegioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegioneServiceImpl implements RegioneService {

    @Autowired
    private RegioneRepository repo;

    @Override
    @SentrySpan
    public List<Regione> getAll() {
        return repo.findAll();
    }

    @Override
    @SentrySpan
    public Optional<Regione> getById(Integer id) {
        return repo.findById(id);
    }
}
