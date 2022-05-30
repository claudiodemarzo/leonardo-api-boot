package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Recensione;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;
import java.util.Optional;

public interface RecensioneService {
	List<Recensione> getByUtenteRecensore(Utente u);

	List<Recensione> getByUtenteRecensito(Utente u);

	Optional<Recensione> getById(Integer id);

}
