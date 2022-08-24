package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnunciLibriRepository extends JpaRepository<AnnunciLibri, Integer> {

    Optional<AnnunciLibri> findById(int id);

    List<AnnunciLibri> findByUtente(UtentePublicInfo utente);

    List<AnnunciLibri> findAllByLibro(Libro l);
}
