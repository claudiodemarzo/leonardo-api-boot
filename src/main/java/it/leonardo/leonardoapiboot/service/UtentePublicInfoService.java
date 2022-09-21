package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UtentePublicInfoService {
    Optional<UtentePublicInfo> getById(Integer id);

    List<UtentePublicInfo> searchUsername(String username);

    Optional<UtentePublicInfo> getByUsername(String username);

    List<UtentePublicInfo> searchNome(String query);

    List<UtentePublicInfo> searchCognome(String query);
}
