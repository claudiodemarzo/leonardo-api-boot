package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.entity.TagsUtenteKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagsUtenteRepository extends JpaRepository<TagsUtente, TagsUtenteKey> {
    List<TagsUtente> findById_Utente(Integer utente);

    void saveAll(List<TagsUtente> tagsUtente);

}
