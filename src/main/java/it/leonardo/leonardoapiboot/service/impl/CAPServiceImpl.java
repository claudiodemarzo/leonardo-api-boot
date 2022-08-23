package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.CAP;
import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.repository.CAPRepository;
import it.leonardo.leonardoapiboot.service.CAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CAPServiceImpl implements CAPService {

    @Autowired
    private CAPRepository capRepository;


    @Override
    @SentrySpan
    public List<CAP> findByCitta(Citta citta) {
        return capRepository.findByCitta(citta);
    }

    @Override
    @SentrySpan
    public Optional<CAP> findByCittaAndCap(Citta citta, String cap) {
        return capRepository.findByCittaAndCap(citta, cap);
    }
}
