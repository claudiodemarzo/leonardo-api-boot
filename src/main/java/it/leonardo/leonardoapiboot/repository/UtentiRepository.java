package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtentiRepository extends JpaRepository<Utente, Integer> {
    Optional<Utente> findAllByEmail(String s);

    Optional<Utente> findAllByUsername(String s);

    Optional<Utente> findAllByUsernameAndPassword(String username, String password);

    Optional<Utente> findAllByUsernameOrEmail(String u, String e);

    Optional<Utente> findAllByResetToken(String s);

    Optional<Utente> findAllByConfirmToken(String s);
}
