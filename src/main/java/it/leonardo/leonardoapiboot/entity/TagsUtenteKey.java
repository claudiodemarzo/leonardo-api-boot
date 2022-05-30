package it.leonardo.leonardoapiboot.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TagsUtenteKey implements Serializable {

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
