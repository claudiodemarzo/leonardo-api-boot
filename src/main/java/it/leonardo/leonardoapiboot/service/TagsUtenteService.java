package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.TagsUtente;

import java.util.List;

public interface TagsUtenteService {

    List<TagsUtente> getTagsByUserId(Integer userID);
}
