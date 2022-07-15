package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
    Optional<Chatroom> findByUtenteMitAndUtenteDest(Utente utenteMit, Utente utenteDest);
}

