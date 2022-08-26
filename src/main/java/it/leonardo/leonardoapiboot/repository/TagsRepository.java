package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tag, Integer> {
}
