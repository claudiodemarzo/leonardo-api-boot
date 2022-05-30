package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Istituto;

import java.util.List;
import java.util.Optional;

public interface IstitutoService {

    List<Istituto> getAll();

    Optional<Istituto> getById(Integer id);

    List<Istituto> getByCitta(Citta c);

    Istituto save(Istituto i);
}
