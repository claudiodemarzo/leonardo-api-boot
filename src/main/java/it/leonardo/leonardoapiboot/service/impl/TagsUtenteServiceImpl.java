package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.TagsUtenteRepository;
import it.leonardo.leonardoapiboot.service.TagsUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagsUtenteServiceImpl implements TagsUtenteService {

    @Autowired
    TagsUtenteRepository repo;

    @Override
    public List<Tag> getTagsByUtente(Utente u) {
        List<TagsUtente> tagsUtente = repo.findTagsUtenteByUtente(u);
        List<Tag> lst = new ArrayList<>();
        tagsUtente.forEach(tu -> lst.add(tu.getTag()));
        return lst;
    }

    @Override
    public List<TagsUtente> getTagsUtenteByUtente(Utente u) {
        return repo.findTagsUtenteByUtente(u);
    }
}
