package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chat_room")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Integer chatroomId;

    @JsonBackReference
    @JoinColumn(name = "utente_mit")
    @ManyToOne
    private Utente utenteMit;

    @JsonBackReference
    @JoinColumn(name = "utente_dest")
    @ManyToOne
    private Utente utenteDest;

    @JsonBackReference
    @OneToMany(mappedBy = "chatroom")
    private List<Messaggio> messaggi;

    public Integer getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Integer chatroomId) {
        this.chatroomId = chatroomId;
    }

    public Utente getUtenteMit() {
        return utenteMit;
    }

    public void setUtenteMit(Utente utenteMit) {
        this.utenteMit = utenteMit;
    }

    public Utente getUtenteDest() {
        return utenteDest;
    }

    public void setUtenteDest(Utente utenteDest) {
        this.utenteDest = utenteDest;
    }

    public List<Messaggio> getMessaggi() {
        return messaggi;
    }

    public void setMessaggi(List<Messaggio> messaggi) {
        this.messaggi = messaggi;
    }

    public Chatroom() {
    }
}
