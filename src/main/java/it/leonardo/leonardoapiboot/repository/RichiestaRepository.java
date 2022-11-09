package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Richiesta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RichiestaRepository extends JpaRepository<Richiesta, Integer> {
}

