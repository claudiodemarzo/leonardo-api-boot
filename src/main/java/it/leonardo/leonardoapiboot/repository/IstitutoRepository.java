package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Citta;
import it.leonardo.leonardoapiboot.entity.Istituto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IstitutoRepository extends JpaRepository<Istituto, Integer> {
    List<Istituto> getIstitutoByCitta(Citta c);
}
