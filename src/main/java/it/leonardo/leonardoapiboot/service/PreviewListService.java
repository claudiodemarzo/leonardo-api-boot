package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.PreviewList;

import java.util.Optional;

public interface PreviewListService {
    PreviewList save(PreviewList p);

    Optional<PreviewList> findByEmailIgnoreCase(String email);
}
