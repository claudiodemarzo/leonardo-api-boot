package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.AnnuncioWithoutRecensione;

import java.util.Optional;

public interface AnnuncioWithoutRecensioneService {

    Optional<AnnuncioWithoutRecensione> findByAnnuncioId(Integer id);

}
