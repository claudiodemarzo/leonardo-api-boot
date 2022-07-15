package it.leonardo.leonardoapiboot.service.impl;

import it.leonardo.leonardoapiboot.entity.ChatRoom;
import it.leonardo.leonardoapiboot.repository.ChatRoomRepository;
import it.leonardo.leonardoapiboot.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist) {

        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId).map(ChatRoom::getChatId).or(() -> {
            if (!createIfNotExist) {
                return Optional.empty();
            }
            String chatId = String.format("%s_%s", senderId, recipientId);

            ChatRoom senderRecipient = new ChatRoom();
            senderRecipient.setChatId(chatId);
            senderRecipient.setSenderId(senderId);
            senderRecipient.setRecipientId(recipientId);

            ChatRoom recipientSender = new ChatRoom();
            recipientSender.setChatId(chatId);
            recipientSender.setSenderId(recipientId);
            recipientSender.setRecipientId(senderId);

            chatRoomRepository.save(senderRecipient);
            chatRoomRepository.save(recipientSender);

            return Optional.of(chatId);
        });
    }
}
