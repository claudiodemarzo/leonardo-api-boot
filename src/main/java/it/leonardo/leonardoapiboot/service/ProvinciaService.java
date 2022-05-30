package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Provincia;
import it.leonardo.leonardoapiboot.entity.Regione;

import java.util.List;
import java.util.Optional;

public interface ProvinciaService {

    List<Provincia> getAll();

    Optional<Provincia> getById(Integer id);

    List<Provincia> getByRegione(Regione r);

}
