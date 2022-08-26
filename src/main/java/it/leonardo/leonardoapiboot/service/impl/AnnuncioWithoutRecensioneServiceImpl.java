package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.AnnuncioWithoutRecensione;
import it.leonardo.leonardoapiboot.repository.AnnuncioWithoutRecensioneRepository;
import it.leonardo.leonardoapiboot.service.AnnuncioWithoutRecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnnuncioWithoutRecensioneServiceImpl implements AnnuncioWithoutRecensioneService {

    @Autowired
    private AnnuncioWithoutRecensioneRepository repository;

    @Override
    public Optional<AnnuncioWithoutRecensione> findByAnnuncioId(Integer id) {
        return repository.findByAnnuncioId(id);
    }
}
