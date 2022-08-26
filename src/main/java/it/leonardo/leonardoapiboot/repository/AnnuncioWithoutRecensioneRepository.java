package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.AnnuncioWithoutRecensione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnuncioWithoutRecensioneRepository extends JpaRepository<AnnuncioWithoutRecensione, Integer> {

    Optional<AnnuncioWithoutRecensione> findByAnnuncioId(Integer id);

}

