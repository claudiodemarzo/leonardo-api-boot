package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.Chatroom;
import it.leonardo.leonardoapiboot.entity.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessaggioRepository extends JpaRepository<Messaggio, Integer> {
    List<Messaggio> findByChatroom(Chatroom chatroom);
}

