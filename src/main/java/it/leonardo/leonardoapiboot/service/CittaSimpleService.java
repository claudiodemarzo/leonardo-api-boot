package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.CittaSimple;

import java.util.List;
import java.util.Optional;

public interface CittaSimpleService {

    Optional<CittaSimple> findAllById(Integer id);

    List<CittaSimple> findAll();
}
