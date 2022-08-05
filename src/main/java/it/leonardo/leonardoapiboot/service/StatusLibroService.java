package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.StatusLibro;

import java.util.Optional;

public interface StatusLibroService {

    Optional<StatusLibro> getStatus(Integer sottCanc, Integer sottNonCanc, Integer scrittCanc, Integer scrittNonCanc, Integer pagManc, Integer pagRov, Integer pagRovMol, Integer copRov, Integer insManc);
}
