package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Provincia;
import it.leonardo.leonardoapiboot.repository.CittaRepository;
import it.leonardo.leonardoapiboot.service.CittaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CittaServiceImpl implements CittaService {

    @Autowired
    private CittaRepository repo;

    @Override
    public List<Citta> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Citta> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Citta> getByProvincia(Provincia p) {
        return repo.findAllByProvincia(p);
    }

}
