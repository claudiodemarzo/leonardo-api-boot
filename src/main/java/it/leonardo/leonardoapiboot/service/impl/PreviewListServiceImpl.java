package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.PreviewList;
import it.leonardo.leonardoapiboot.repository.PreviewListRepository;
import it.leonardo.leonardoapiboot.service.PreviewListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreviewListServiceImpl implements PreviewListService {

    @Autowired
    private PreviewListRepository repo;

    @Override
    public PreviewList save(PreviewList p) {
        return repo.save(p);
    }

    @Override
    public Optional<PreviewList> findByEmailIgnoreCase(String email) {
        return repo.findByEmailIgnoreCase(email);
    }
}
