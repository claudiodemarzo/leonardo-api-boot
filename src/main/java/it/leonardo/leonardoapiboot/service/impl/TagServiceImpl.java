package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.Tag;
import it.leonardo.leonardoapiboot.repository.TagsRepository;
import it.leonardo.leonardoapiboot.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagsRepository repo;

    @Override
    public List<Tag> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Tag> getById(Integer id) {
        return repo.findById(id);
    }
}
