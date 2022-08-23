package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtentePublicInfoRepository extends JpaRepository<UtentePublicInfo, Integer> {

    List<UtentePublicInfo> findAllByUsernameLikeIgnoreCase(String username);

    Optional<UtentePublicInfo> findByUsername(String username);
}
