package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TagsUtenteRepository extends JpaRepository<TagsUtente, Integer> {

    List<TagsUtente> findTagsUtenteByUtente(Utente u);

}
