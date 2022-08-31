package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Carrello;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;

public interface CarrelloService {
	List<Carrello> getByUtente(Utente u);

	Carrello save(Carrello c);

	void deleteById(Integer id);

	void deleteByAnnuncio(AnnunciLibri ann);

	void deleteAllByUtente(Utente u);
}
