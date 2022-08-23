package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.TagsUtenteRepository;
import it.leonardo.leonardoapiboot.service.TagsUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsUtenteServiceImpl implements TagsUtenteService {

    @Autowired
    TagsUtenteRepository repo;

    @Override
    @SentrySpan
    public List<Tag> getTagsByUtente(Utente u) {
        List<TagsUtente> tagsUtente = repo.findTagsUtenteByUtente(u);
        List<Tag> lst = new ArrayList<>();
        tagsUtente.forEach(tu -> lst.add(tu.getTag()));
        return lst;
    }

    @Override
    @SentrySpan
    public List<TagsUtente> getTagsUtenteByUtente(Utente u) {
        return repo.findTagsUtenteByUtente(u);
    }

    @Override
    @SentrySpan
    public TagsUtente save(TagsUtente tu) {
        return repo.save(tu);
    }

    @Override
    @SentrySpan
    public List<TagsUtente> saveAll(List<TagsUtente> tuList) {
        return repo.saveAll(tuList);
    }
}
