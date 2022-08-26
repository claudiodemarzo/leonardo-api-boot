package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagsRepository extends JpaRepository<Tag, Integer> {
}