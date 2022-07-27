package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.CAP;
import it.leonardo.leonardoapiboot.entity.Citta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CAPRepository extends JpaRepository<CAP, Integer> {
    List<CAP> findByCitta(Citta citta);

    Optional<CAP> findByCittaAndCap(Citta citta, String cap);
}
