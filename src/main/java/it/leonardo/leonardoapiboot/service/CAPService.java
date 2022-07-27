package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.CAP;
import it.leonardo.leonardoapiboot.entity.Citta;

import java.util.List;
import java.util.Optional;

public interface CAPService {

    List<CAP> findByCitta(Citta citta);

    Optional<CAP> findByCittaAndCap(Citta citta, String cap);
}
