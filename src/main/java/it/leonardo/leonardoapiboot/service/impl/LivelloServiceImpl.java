package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Livello;
import it.leonardo.leonardoapiboot.repository.LivelliRepository;
import it.leonardo.leonardoapiboot.service.LivelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivelloServiceImpl implements LivelloService {

    @Autowired
    private LivelliRepository repo;

    @Override
    @SentrySpan
    public List<Livello> getAll() {
        return repo.findAll();
    }

    @Override
    @SentrySpan
    public Optional<Livello> getById(Integer id) {
        return repo.findById(id);
    }
}
