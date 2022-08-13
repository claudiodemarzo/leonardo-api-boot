package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messaggi")
public class Messaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messaggio_id")
    private Integer messaggioId;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "chatroom")
    private Chatroom chatroom;

    @Column(name = "messaggio")
    private String messaggio;

    @Column(name = "status")
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    public Integer getMessaggioId() {
        return messaggioId;
    }

    public void setMessaggioId(Integer messaggioId) {
        this.messaggioId = messaggioId;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Messaggio{" +
                "messaggioId=" + messaggioId +
                ", chatroom=" + chatroom +
                ", messaggio='" + messaggio + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }

    public Messaggio() {
    }
}
