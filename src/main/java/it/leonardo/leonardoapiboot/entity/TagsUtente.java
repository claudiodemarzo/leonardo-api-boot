package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "tags_utenti")
public class TagsUtente {

    @EmbeddedId
    @JsonIgnore
    private TagsUtenteKey id;

    @ManyToOne
    @MapsId("tag")
    @JoinColumn(name = "tag")
    private Tag tag;

    @JsonBackReference
    @ManyToOne
    @MapsId("utente")
    @JoinColumn(name = "utente")
    private Utente utente;

    @Column(name = "active")
    private Boolean active;

    public TagsUtenteKey getId() {
        return id;
    }

    public void setId(TagsUtenteKey id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public TagsUtente() {
    }
}
