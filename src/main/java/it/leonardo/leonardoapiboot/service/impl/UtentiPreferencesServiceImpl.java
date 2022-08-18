package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.UtentiPreferences;
import it.leonardo.leonardoapiboot.repository.UtentiPreferencesRepository;
import it.leonardo.leonardoapiboot.service.UtentiPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtentiPreferencesServiceImpl implements UtentiPreferencesService {

    @Autowired
    private UtentiPreferencesRepository repo;

    @Override
    @SentrySpan
    public Optional<UtentiPreferences> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public UtentiPreferences save(UtentiPreferences up) {
        return repo.save(up);
    }
}
