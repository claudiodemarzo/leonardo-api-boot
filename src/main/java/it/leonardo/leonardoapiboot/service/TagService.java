package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAll();

    Optional<Tag> getById(Integer id);

    Optional<Tag> getActiveTagByUtente(Integer id);
}
