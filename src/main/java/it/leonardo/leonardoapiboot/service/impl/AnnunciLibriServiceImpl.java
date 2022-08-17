package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.AnnunciLibriRepository;
import it.leonardo.leonardoapiboot.service.AnnunciLibriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnunciLibriServiceImpl implements AnnunciLibriService {

    @Autowired
    private AnnunciLibriRepository repo;

    @Override
    public List<AnnunciLibri> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<AnnunciLibri> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<AnnunciLibri> getByUtente(Utente u) {
        return repo.findAllByUtente(u);
    }

    @Override
    public List<AnnunciLibri> getByLibro(Libro l) {
        return repo.findAllByLibro(l);
    }

    @Override
    public AnnunciLibri save(AnnunciLibri al) {
        return repo.save(al);
    }
}
