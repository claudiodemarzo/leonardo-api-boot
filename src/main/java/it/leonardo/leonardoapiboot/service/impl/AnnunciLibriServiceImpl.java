package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.AnnunciLibri;
import it.leonardo.leonardoapiboot.entity.Libro;
import it.leonardo.leonardoapiboot.entity.Utente;
import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;
import it.leonardo.leonardoapiboot.repository.AnnunciLibriRepository;
import it.leonardo.leonardoapiboot.repository.LibriRepository;
import it.leonardo.leonardoapiboot.service.AnnunciLibriService;
import it.leonardo.leonardoapiboot.service.UtentePublicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AnnunciLibriServiceImpl implements AnnunciLibriService {

    @Autowired
    private AnnunciLibriRepository repo;

    @Autowired
    private LibriRepository libriRepo;

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
        return repo.findAllBySoldToAndStato(upf, 2);
    }

    @Override
    @SentrySpan
    public AnnunciLibri save(AnnunciLibri al) {
        return repo.save(al);
    }

    @Override
    @SentrySpan
    public List<AnnunciLibri> searchFilters(String query, String stato, Float prezzoMax, String orderBy) {
        List<Libro> lstLibri = libriRepo.findAllByIsbnLikeIgnoreCase("%" + query + "%");
        lstLibri.addAll(libriRepo.findAllByNomeLikeIgnoreCase("%" + query + "%"));
        lstLibri.addAll(libriRepo.findAllByAutoriLikeIgnoreCase("%" + query + "%"));
        lstLibri.addAll(libriRepo.findAllByCasaedLikeIgnoreCase("%" + query + "%"));
        lstLibri.addAll(libriRepo.findAllByCategoriaLikeIgnoreCase("%" + query + "%"));

        List<Integer> uniqueIDs = new ArrayList<>();

        for (Libro l : lstLibri) {
            if (!uniqueIDs.contains(l.getLibroId())) {
                uniqueIDs.add(l.getLibroId());
            }else{
                lstLibri.remove(l);
            }
        }

        List<AnnunciLibri> lstAnnunci = new ArrayList<>();
        lstLibri.forEach(l -> lstAnnunci.addAll(l.getAnnunci()));

        List<AnnunciLibri> lstAnnunciFiltered = new ArrayList<>();
        lstAnnunci.forEach(a -> {
            if (a.getStato() == 1)
                if (stato.equalsIgnoreCase("t") || (a.getLivello_usura() + "").equalsIgnoreCase(stato))
                    if (prezzoMax == -1 || a.getPrezzo() <= prezzoMax)
                        lstAnnunciFiltered.add(a);
        });

        lstAnnunciFiltered.sort((a1, a2) -> switch (orderBy.toLowerCase()) {
            case "prezzoasc" -> a1.getPrezzo().compareTo(a2.getPrezzo());
            case "prezzodesc" -> a2.getPrezzo().compareTo(a1.getPrezzo());
            case "rec" -> {
                if (a1.getUtente().getAvgRating() == null) yield 1;
                if (a2.getUtente().getAvgRating() == null) yield -1;
                yield a2.getUtente().getAvgRating().getAvgVoto().compareTo(a1.getUtente().getAvgRating().getAvgVoto());
            }
            case "data" -> a2.getCreated_at().compareTo(a1.getCreated_at());
            default -> 0;
        });

        return lstAnnunciFiltered;
    }
}
