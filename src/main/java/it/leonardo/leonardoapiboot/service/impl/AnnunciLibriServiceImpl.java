package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;
import it.leonardo.leonardoapiboot.repository.AnnunciLibriRepository;
import it.leonardo.leonardoapiboot.service.AnnunciLibriService;
import it.leonardo.leonardoapiboot.service.UtentePublicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnunciLibriServiceImpl implements AnnunciLibriService {

    @Autowired
    private AnnunciLibriRepository repo;

    @Autowired
    private UtentePublicInfoService utentePublicInfoService;

    @Override
    @SentrySpan
    public List<AnnunciLibri> getAll() {
        return repo.findAll();
    }

    @Override
    @SentrySpan
    public Optional<AnnunciLibri> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public List<AnnunciLibri> getByUtente(UtentePublicInfo u) {
        return repo.findByUtente(u);
    }

    @Override
    @SentrySpan
    public List<AnnunciLibri> getByLibro(Libro l) {
        return repo.findAllByLibro(l);
    }

    @Override
    @SentrySpan
    public List<AnnunciLibri> getAnnunciAcquistati(Utente u) {
        UtentePublicInfo upf = utentePublicInfoService.getById(u.getUtenteId()).get();
        return repo.findAllBySoldToAndStato(upf, 1);
    }

    @Override
    @SentrySpan
    public AnnunciLibri save(AnnunciLibri al) {
        return repo.save(al);
    }
}
