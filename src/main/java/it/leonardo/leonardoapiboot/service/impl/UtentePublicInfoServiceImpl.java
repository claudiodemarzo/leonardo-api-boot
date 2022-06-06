package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;
import it.leonardo.leonardoapiboot.repository.UtentePublicInfoRepository;
import it.leonardo.leonardoapiboot.service.UtentePublicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtentePublicInfoServiceImpl implements UtentePublicInfoService {

    @Autowired
    private UtentePublicInfoRepository repo;

    @Override
    public Optional<UtentePublicInfo> getById(Integer id) {
        return repo.findById(id);
    }
}
