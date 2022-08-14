package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
    Optional<Chatroom> findByUtenteMitAndUtenteDest(Utente utenteMit, Utente utenteDest);
    List<Chatroom> findAllByUtenteMit(Utente utenteMit);
    List<Chatroom> findAllByUtenteDest(Utente utenteDest);
}

