package it.leonardo.leonardoapiboot.repository;

import it.leonardo.leonardoapiboot.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    long countBySenderIdAndRecipientIdAndStatus(Integer senderId, Integer recipientId, Integer status);

    List<ChatMessage> findByChatId(String chatId);

    List<ChatMessage> findBySenderIdAndRecipientId(Integer senderId, Integer recipientId);
}