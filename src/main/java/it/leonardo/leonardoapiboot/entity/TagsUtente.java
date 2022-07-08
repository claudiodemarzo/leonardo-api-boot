package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tags_utenti")
@IdClass(TagsUtente.TagsUtenteKey.class)
public class TagsUtente {

    @EmbeddedId
    @JsonIgnore
    private TagsUtenteKey id;

    @MapsId("tag")
    @ManyToOne
    @JoinColumn(name = "tag")
    private Tag tag;

    @MapsId("utente")
    @JsonBackReference
    @ManyToOne
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

    @Embeddable
    public static class TagsUtenteKey implements Serializable {

        @Column(name = "tag")
        private Integer tag;

        @Column(name = "utente")
        private Integer utente;

        public Integer getTag() {
            return tag;
        }

        public void setTag(Integer tag) {
            this.tag = tag;
        }

        public Integer getUtente() {
            return utente;
        }

        public void setUtente(Integer utente) {
            this.utente = utente;
        }

        public TagsUtenteKey() {
        }

        public TagsUtenteKey(Integer tag, Integer utente) {
            this.tag = tag;
            this.utente = utente;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TagsUtenteKey that = (TagsUtenteKey) o;
            return Objects.equals(tag, that.tag) && Objects.equals(utente, that.utente);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tag, utente);
        }
    }
}
