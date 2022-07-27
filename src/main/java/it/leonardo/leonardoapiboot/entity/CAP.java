package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "cap_citta")
public class CAP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ass")
    @JsonIgnore
    private Integer idAss;

    @ManyToOne
    @JoinColumn(name = "citta_id")
    @JsonBackReference
    private Citta citta;

    @Column(name = "cap")
    private String cap;

    public Integer getIdAss() {
        return idAss;
    }

    public void setIdAss(Integer idAss) {
        this.idAss = idAss;
    }

    public Citta getCitta() {
        return citta;
    }

    public void setCitta(Citta citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public CAP() {
    }
}
