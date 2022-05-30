package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Carrello;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.CarrelloRepository;
import it.leonardo.leonardoapiboot.service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrelloServiceImpl implements CarrelloService {

	@Autowired
	private CarrelloRepository repo;

	@Override
	public List<Carrello> getByUtente(Utente u) {
		return repo.findAllByUtente(u);
	}

	@Override
	public Carrello save(Carrello c) {
		return repo.save(c);
	}

	@Override
	public void deleteById(Integer id) {
		repo.deleteCarrelloByEntryId(id);
	}

	@Override
	public void deleteAllByUtente(Utente u){
		repo.deleteCarrelloByUtente(u);
	}
}
