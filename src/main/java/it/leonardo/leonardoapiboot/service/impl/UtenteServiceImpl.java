package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.UtentiRepository;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtentiRepository repo;

    @Override
    @SentrySpan
    public Optional<Utente> findByUsername(String s) {
        return repo.findAllByUsername(s);
    }

    @Override
    @SentrySpan
    public Optional<Utente> findByEmail(String e) {
        return repo.findByEmail(e);
    }

    @Override
    @SentrySpan
    public Optional<Utente> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public Optional<Utente> findByUsernameAndPassword(String username, String hashedPass) {
        return repo.findAllByUsernameAndPassword(username, hashedPass);
    }

    @Override
    @SentrySpan
    public Optional<Utente> findByUsernameOrEmail(String query) {
        return repo.findAllByUsernameOrEmail(query, query);
    }

    @Override
    @SentrySpan
    public Optional<Utente> findByResetToken(String token) {
        return repo.findAllByResetToken(token);
    }

    @Override
    @SentrySpan
    public Optional<Utente> findByConfirmToken(String token) {
        return repo.findAllByConfirmToken(token);
    }

    @Override
    public void setOnlineStatus(boolean onlineStatus, Integer utenteID) {
        Utente u = repo.findById(utenteID).get();
        u.setOnline(onlineStatus);
        repo.save(u);
    }

    @Override
    @SentrySpan
    public Utente save(Utente u) {
        return repo.saveAndFlush(u);
    }
}
