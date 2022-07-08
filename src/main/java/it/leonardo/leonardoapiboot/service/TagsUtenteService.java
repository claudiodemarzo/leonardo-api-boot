package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;
import java.util.Optional;

public interface TagsUtenteService {

    List<Tag> getTagsByUtente(Utente u);
    Optional<TagsUtente> getTagsUtenteById(TagsUtente.TagsUtenteKey id);

    List<TagsUtente> getTagsUtenteByUtente(Utente u);
}
