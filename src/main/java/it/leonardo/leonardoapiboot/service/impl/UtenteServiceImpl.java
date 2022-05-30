package it.leonardo.leonardoapiboot.service.impl;

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
    public Optional<Utente> findByUsername(String s) {
        return repo.findAllByUsername(s);
    }

    @Override
    public Optional<Utente> findByEmail(String e) {
        return repo.findAllByEmail(e);
    }

    @Override
    public Optional<Utente> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Utente> findByUsernameAndPassword(String username, String hashedPass) {
        return repo.findAllByUsernameAndPassword(username, hashedPass);
    }

    @Override
    public Utente save(Utente u){
        return repo.saveAndFlush(u);
    }
}
