package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Richiesta;

import java.util.Optional;

public interface RichiestaService {
    Richiesta save(Richiesta richiesta);

    Optional<Richiesta> findById(Integer id);
}
