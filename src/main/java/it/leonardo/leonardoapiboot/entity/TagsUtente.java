package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tags_utenti")
public class TagsUtente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tagsutenti")
    @JsonIgnore
    private Integer idTagsUtente;

    @ManyToOne
    @JoinColumn(name = "tag")
    private Tag tag;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "utente")
    @JsonIgnore
    private Utente utente;

    @Column(name = "active")
    private Boolean active;

    public Integer getIdTagsUtente() {
        return idTagsUtente;
    }

    public void setIdTagsUtente(Integer idTagsUtente) {
        this.idTagsUtente = idTagsUtente;
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
