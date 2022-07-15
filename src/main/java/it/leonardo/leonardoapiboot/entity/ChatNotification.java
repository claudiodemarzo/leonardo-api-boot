package it.leonardo.leonardoapiboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chat_notification")
public class ChatNotification {

    @Id
    @Column(name = "chatnotification_id")
    private String id;

    @Column(name = "mittente_id")
    private Integer senderId;

    @Column(name = "mittente_nome")
    private String senderName;

    public ChatNotification(Integer id, Integer senderId, String senderName) {
        this.id = id.toString();
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public ChatNotification() {
    }
}
