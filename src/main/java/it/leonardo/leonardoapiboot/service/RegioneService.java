package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Regione;

import java.util.List;
import java.util.Optional;

public interface RegioneService {

    List<Regione> getAll();

    Optional<Regione> getById(Integer id);

}
