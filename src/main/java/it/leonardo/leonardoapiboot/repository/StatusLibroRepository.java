package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.StatusLibro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusLibroRepository extends JpaRepository<StatusLibro, Integer> {
    Optional<StatusLibro> findBySottCancAndSottNonCancAndScrittCancAndScrittNonCancAndPagMancAndPagRovAndPagRovMolAndCopRovAndInsManc(Integer sottCanc, Integer sottNonCanc, Integer scrittCanc, Integer scrittNonCanc, Integer pagManc, Integer pagRov, Integer pagRovMol, Integer copRov, Integer insManc);

}