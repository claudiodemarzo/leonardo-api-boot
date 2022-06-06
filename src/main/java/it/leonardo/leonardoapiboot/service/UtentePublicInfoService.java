package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;

import java.util.Optional;

public interface UtentePublicInfoService {
    Optional<UtentePublicInfo> getById(Integer id);
}
