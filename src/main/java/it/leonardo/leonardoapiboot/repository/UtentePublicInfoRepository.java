package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.UtentePublicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtentePublicInfoRepository extends JpaRepository<UtentePublicInfo, Integer> {

    List<UtentePublicInfo> findAllByUsernameLikeIgnoreCase(String username);
}
