package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.CittaSimple;
import it.leonardo.leonardoapiboot.repository.CittaSimpleRepository;
import it.leonardo.leonardoapiboot.service.CittaSimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CittaSimpleServiceImpl implements CittaSimpleService {

    @Autowired
    private CittaSimpleRepository repo;

    @Override
    public Optional<CittaSimple> findAllById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<CittaSimple> findAll() {
        return repo.findAll();
    }
}
