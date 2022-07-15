package it.leonardo.leonardoapiboot.service;

import it.leonardo.leonardoapiboot.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    long countNewMessages(Integer senderId, Integer recipientId);

    List<ChatMessage> findChatMessages(Integer senderId, Integer recipientId);

    ChatMessage findById(Integer id) throws Exception;

    void updateStatuses(Integer mittenteId, Integer destinatarioId, Integer stato);
}
