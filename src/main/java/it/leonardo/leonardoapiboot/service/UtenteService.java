package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.Optional;

public interface UtenteService {

    Optional<Utente> findByUsername(String s);

    Optional<Utente> findByEmail(String e);

    Optional<Utente> findById(Integer id);

    Optional<Utente> findByUsernameAndPassword(String username, String hashedPass);

    Optional<Utente> findByUsernameOrEmail(String query);

    Optional<Utente> findByResetToken(String token);

    Optional<Utente> findByConfirmToken(String token);

    void setOnlineStatus(boolean onlineStatus, Integer utenteID);

    Utente save(Utente u);
}
