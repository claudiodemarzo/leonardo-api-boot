package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.CAP;
import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.repository.CAPRepository;
import it.leonardo.leonardoapiboot.service.CAPService;
import it.leonardo.leonardoapiboot.service.CittaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CAPServiceImpl implements CAPService {

    @Autowired
    private CAPRepository capRepository;


    @Override
    public List<CAP> findByCitta(Citta citta) {
        return capRepository.findByCitta(citta);
    }

    @Override
    public Optional<CAP> findByCittaAndCap(Citta citta, String cap) {
        return capRepository.findByCittaAndCap(citta, cap);
    }
}
