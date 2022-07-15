package it.leonardo.leonardoapiboot.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
   @Id
   @Column(name = "chatmessage_id")
   private Integer id;

   @Column(name = "chatroom_id")
   private String chatId;

   @Column(name = "mittente_id")
   private Integer senderId;

   @Column(name = "destinatario_id")
   private Integer recipientId;

   @Column(name = "mittente_nome")
   private String senderName;

   @Column(name = "destinatario_nome")
   private String recipientName;

   @Column(name = "messaggio")
   private String content;

   @Column(name = "timestamp")
   @Temporal(TemporalType.TIMESTAMP)
   private Date timestamp;

   @Column(name = "stato")
   private Integer status;

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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ChatMessage() {
    }
}
