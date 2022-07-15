package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.ChatMessage;
import it.leonardo.leonardoapiboot.exceptions.ResourceNotFoundException;
import it.leonardo.leonardoapiboot.repository.ChatMessageRepository;
import it.leonardo.leonardoapiboot.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageRepository repository;
    @Autowired
    private ChatRoomServiceImpl chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(0);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Integer senderId, Integer recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, 0);
    }

    public List<ChatMessage> findChatMessages(Integer senderId, Integer recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> messages = chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderId, recipientId, 1);
        }

        return messages;
    }

    public ChatMessage findById(Integer id) {
        return repository.findById(id).map(chatMessage -> {
            chatMessage.setStatus(1);
            return repository.save(chatMessage);
        }).orElseThrow(() -> new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(Integer senderId, Integer recipientId, Integer status) {
        List<ChatMessage> messages = repository.findBySenderIdAndRecipientId(senderId, recipientId);
        messages.forEach(message -> {
            message.setStatus(status);
        });
        repository.saveAll(messages);
    }
}
