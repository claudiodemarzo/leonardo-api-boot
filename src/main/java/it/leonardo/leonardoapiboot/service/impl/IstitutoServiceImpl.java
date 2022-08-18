package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Istituto;
import it.leonardo.leonardoapiboot.repository.IstitutoRepository;
import it.leonardo.leonardoapiboot.service.IstitutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IstitutoServiceImpl implements IstitutoService {

    @Autowired
    private IstitutoRepository repo;

    @Override
    @SentrySpan
    public List<Istituto> getAll() {
        return repo.findAll();
    }

    @Override
    @SentrySpan
    public Optional<Istituto> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public List<Istituto> getByCitta(Citta c) {
        return repo.getIstitutoByCitta(c);
    }

    @Override
    @SentrySpan
    public Istituto save(Istituto i) {
        return repo.save(i);
    }
}
