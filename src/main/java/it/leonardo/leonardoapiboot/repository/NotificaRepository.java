package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Notifica;
import it.leonardo.leonardoapiboot.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificaRepository extends JpaRepository<Notifica, Integer> {

    List<Notifica> findAllByUtente(Utente utente);

    List<Notifica> findAllByUtenteAndCancellatoIsFalse(Utente utente);

    List<Notifica> findAllByUtenteAndLettoIsFalseAndCancellatoIsFalse(Utente utente);
}
