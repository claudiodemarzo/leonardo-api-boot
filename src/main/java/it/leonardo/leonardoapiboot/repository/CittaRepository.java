package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CittaRepository extends JpaRepository<Citta, Integer> {
    List<Citta> findAllByProvincia(Provincia p);
}
