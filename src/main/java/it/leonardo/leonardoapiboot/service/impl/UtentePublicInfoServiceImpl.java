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
        return repo.findById(id);
    }

    @Override
    @SentrySpan
    public List<UtentePublicInfo> searchUsername(String username) {
        return repo.findAllByUsernameLikeIgnoreCase("%" + username + "%");
    }
}
