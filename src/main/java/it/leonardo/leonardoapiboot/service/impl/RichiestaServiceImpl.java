package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Richiesta;
import it.leonardo.leonardoapiboot.repository.RichiestaRepository;
import it.leonardo.leonardoapiboot.service.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RichiestaServiceImpl implements RichiestaService {

    @Autowired
    private RichiestaRepository repo;

    @Override
    public Richiesta save(Richiesta richiesta) {
        return repo.save(richiesta);
    }
}

