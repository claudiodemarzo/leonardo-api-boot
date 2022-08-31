package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Carrello;
import it.leonardo.leonardoapiboot.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {
	List<Carrello> findAllByUtente(Utente u);

	void deleteCarrelloByEntryId(Integer id);

	List<Carrello> findAllByAnnuncio(AnnunciLibri ann);

	void deleteCarrelloByAnnuncio(AnnunciLibri ann);

	void deleteCarrelloByUtente(Utente u);
}
