package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.Recensione;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.repository.RecensioneRepository;
import it.leonardo.leonardoapiboot.service.RecensioneService;
import it.leonardo.leonardoapiboot.service.UtentePublicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecensioneServiceImpl implements RecensioneService {

    @Autowired
    private RecensioneRepository repo;

    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

    @Override
    @SentrySpan
    public List<Recensione> getByUtenteRecensore(Utente u) {
        List<Recensione> recList = repo.findAllByUtenteRecensore(u);

        recList.forEach(r -> {
            r.setUtenteRecensitoPublicInfo(utentePublicInfoService.getById(r.getUtenteRecensito().getUtenteId()).get());
            r.setUtenteRecensorePublicInfo(utentePublicInfoService.getById(r.getUtenteRecensore().getUtenteId()).get());
        });

        return recList;
    }

    @Override
    @SentrySpan
    public List<Recensione> getByUtenteRecensito(Utente u) {
        List<Recensione> recList = repo.findAllByUtenteRecensito(u);

        recList.forEach(r -> {
            r.setUtenteRecensitoPublicInfo(utentePublicInfoService.getById(r.getUtenteRecensito().getUtenteId()).get());
            r.setUtenteRecensorePublicInfo(utentePublicInfoService.getById(r.getUtenteRecensore().getUtenteId()).get());
        });

        return recList;
    }

    @Override
    @SentrySpan
    public Optional<Recensione> getById(Integer id) {
        Optional<Recensione> rec = repo.findById(id);
        if(rec.isPresent()){
            Recensione recObj = rec.get();
            recObj.setUtenteRecensorePublicInfo(utentePublicInfoService.getById(recObj.getUtenteRecensore().getUtenteId()).get());
            recObj.setUtenteRecensitoPublicInfo(utentePublicInfoService.getById(recObj.getUtenteRecensito().getUtenteId()).get());
            return Optional.of(recObj);
        }

        return Optional.empty();
    }

    @Override
    @SentrySpan
    public Recensione save(Recensione r) {
        return repo.save(r);
    }
}
