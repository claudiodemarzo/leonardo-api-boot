package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;

import java.util.List;
import java.util.Optional;

public interface AnnunciLibriService {

    List<AnnunciLibri> getAll();

    Optional<AnnunciLibri> findById(Integer id);

    List<AnnunciLibri> getByUtente(UtentePublicInfo u);

    List<AnnunciLibri> getByLibro(Libro l);

    AnnunciLibri save(AnnunciLibri al);

}
