package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.TagsUtente;
import it.leonardo.leonardoapiboot.repository.TagsUtenteRepository;
import it.leonardo.leonardoapiboot.service.TagsUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsUtenteServiceImpl implements TagsUtenteService {

    @Autowired
    TagsUtenteRepository repo;

    @Override
    public List<TagsUtente> getTagsByUserId(Integer userID) {
        return repo.findById_Utente(userID);
    }
}
