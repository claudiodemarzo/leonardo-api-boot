package it.leonardo.leonardoapiboot.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @Column(name = "chatroom_id")
    private Integer id;

    @Column(name = "chatroom_name")
    private String chatId;

    @Column(name = "mittente_id")
    private Integer senderId;

    @Column(name = "destinatario_id")
    private Integer recipientId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public ChatRoom() {
    }
}
