package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.PreviewList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreviewListRepository extends JpaRepository<PreviewList, Integer> {

    Optional<PreviewList> findByEmailIgnoreCase(String email);
}
