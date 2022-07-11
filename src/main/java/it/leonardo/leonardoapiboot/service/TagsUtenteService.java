package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.entity.Utente;

import java.util.List;

public interface TagsUtenteService {

    List<Tag> getTagsByUtente(Utente u);

    List<TagsUtente> getTagsUtenteByUtente(Utente u);

    TagsUtente save(TagsUtente tu);

    List<TagsUtente> saveAll(List<TagsUtente> tuList);
}
