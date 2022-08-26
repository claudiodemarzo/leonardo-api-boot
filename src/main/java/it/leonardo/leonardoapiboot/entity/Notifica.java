package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifiche")
public class Notifica {
    public enum TipoNotifica {
        info,
        success,
        warning,
        error
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notifica_id")
    private Integer notificaId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private TipoNotifica type;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "letto")
    private Boolean letto = false;

    @Column(name = "cancellato")
    private Boolean cancellato = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "utente")
    private Utente utente;

    public Notifica(TipoNotifica type, String title, String content, Utente utente) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.letto = false;
        this.utente = utente;
    }

    public Notifica() {
    }

    public TipoNotifica getType() {
        return type;
    }

    public void setType(TipoNotifica type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNotificaId() {
        return notificaId;
    }

    public void setNotificaId(Integer notificaId) {
        this.notificaId = notificaId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getLetto() {
        return letto;
    }

    public void setLetto(Boolean letto) {
        this.letto = letto;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Boolean getCancellato() {
        return cancellato;
    }

    public void setCancellato(Boolean cancellato) {
        this.cancellato = cancellato;
    }

    @Override
    public String toString() {
        String obj = new JSONObject()
                .put("notificaId", notificaId)
                .put("type", type)
                .put("title", title)
                .put("content", content)
                .put("timestamp", timestamp.toInstant().toString())
                .put("letto", letto)
                .toString();
        return obj;
    }
}
