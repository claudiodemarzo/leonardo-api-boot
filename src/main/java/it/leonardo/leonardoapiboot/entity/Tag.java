package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "nome", length = 50)
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @OneToMany(mappedBy = "activeTag")
    @JsonBackReference
    private List<Utente> utenti;

    @OneToMany(mappedBy = "activeTag")
    @JsonBackReference
    private List<UtentePublicInfo> tagsPub;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(List<Utente> utenti) {
        this.utenti = utenti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getTagId(), tag.getTagId()) && Objects.equals(getNome(), tag.getNome()) && Objects.equals(getDescrizione(), tag.getDescrizione());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagId(), getNome(), getDescrizione());
    }

    public Tag() {
    }
}
