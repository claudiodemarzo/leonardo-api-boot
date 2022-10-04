package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.json.JSONObject;

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

    @Column(name = "tipo")
    private String tipo;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("messaggioId", this.messaggioId)
                .put("senderId", this.chatroom.getUtenteMit().getUtenteId())
                .put("senderUsername", this.chatroom.getUtenteMit().getUsername())
                .put("messaggio", this.messaggio)
                .put("status", this.status)
                .put("timestamp", this.timestamp.toInstant().toString())
                .put("tipo", this.tipo)
                .toString();
    }

    public Messaggio() {
    }

}
