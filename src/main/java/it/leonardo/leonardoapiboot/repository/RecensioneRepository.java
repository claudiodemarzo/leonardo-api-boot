package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Recensione;
import it.leonardo.leonardoapiboot.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecensioneRepository extends JpaRepository<Recensione, Integer> {

	List<Recensione> findAllByUtenteRecensito(Utente u);

	List<Recensione> findAllByUtenteRecensore(Utente u);
}
