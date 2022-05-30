package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Livello;

import java.util.List;
import java.util.Optional;

public interface LivelloService {
    List<Livello> getAll();

    Optional<Livello> getById(Integer id);
}
