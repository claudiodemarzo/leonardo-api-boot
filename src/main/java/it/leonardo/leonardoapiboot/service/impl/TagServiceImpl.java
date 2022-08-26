package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.repository.TagsRepository;
import it.leonardo.leonardoapiboot.repository.TagsUtenteRepository;
import it.leonardo.leonardoapiboot.service.TagService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagsRepository repo;

    @Autowired
    private TagsUtenteRepository tagsUtenteRepository;

    @Autowired
    private UtenteService utenteService;

    @Override
    @SentrySpan
    public List<Tag> getAll() {
        return repo.findAll();
    }

    @Override
    @SentrySpan
    public Optional<Tag> getById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public Optional<Tag> getActiveTagByUtente(Integer id) {
        return Optional.of(tagsUtenteRepository.findTagsUtenteByUtente(utenteService.findById(id).get()).stream().filter(TagsUtente::getActive).findFirst().get().getTag());
    }
}
