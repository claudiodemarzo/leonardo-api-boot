package it.leonardo.leonardoapiboot.service.impl;

import io.sentry.spring.tracing.SentrySpan;
import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;
import it.leonardo.leonardoapiboot.repository.UtentePublicInfoRepository;
import it.leonardo.leonardoapiboot.service.UtentePublicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtentePublicInfoServiceImpl implements UtentePublicInfoService {

    @Autowired
    private UtentePublicInfoRepository repo;

    @Override
    @SentrySpan
    public Optional<UtentePublicInfo> getById(Integer id) {
        return adjustLibriVenduti(repo.findById(id));
    }

    @Override
    @SentrySpan
    public List<UtentePublicInfo> searchUsername(String username) {
        return adjustLibriVenduti(repo.findAllByUsernameLikeIgnoreCase("%" + username + "%"));
    }

    @Override
    @SentrySpan
    public Optional<UtentePublicInfo> getByUsername(String username) {
        return adjustLibriVenduti(repo.findByUsername(username));
    }

    private Optional<UtentePublicInfo> adjustLibriVenduti(Optional<UtentePublicInfo> opt) {
        if (opt.isEmpty()) return Optional.empty();

        UtentePublicInfo utente = opt.get();
        utente.setLibriVenduti(utente.getLibriVenduti() == null ? 0 : utente.getLibriVenduti());
        return Optional.of(utente);
    }

    private List<UtentePublicInfo> adjustLibriVenduti(List<UtentePublicInfo> lst){
        for(UtentePublicInfo utente : lst){
            utente.setLibriVenduti(utente.getLibriVenduti() == null ? 0 : utente.getLibriVenduti());
        }
        return lst;
    }
}
