package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Provincia;

import java.util.List;
import java.util.Optional;

public interface CittaService {
    List<Citta> getAll();

    Optional<Citta> getById(Integer id);

    List<Citta> getByProvincia(Provincia p);

}
