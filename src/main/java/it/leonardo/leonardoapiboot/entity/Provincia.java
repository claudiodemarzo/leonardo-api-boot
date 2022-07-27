package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "provincia")
public class Provincia implements Serializable {

    @Id
    @Column(name = "provincia_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer provinciaId;

    @Column(name = "nome", length = 2)
    private String nome;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "regione")
    private Regione regione;

    @OneToMany(mappedBy = "provincia")
    @JsonBackReference
    private List<Citta> cittas;

    public Integer getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Integer provinciaId) {
        this.provinciaId = provinciaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Regione getRegione() {
        return regione;
    }

    public void setRegione(Regione regione) {
        this.regione = regione;
    }

    public List<Citta> getCittas() {
        return cittas;
    }

    public void setCittas(List<Citta> cittas) {
        this.cittas = cittas;
    }

    public Provincia() {
    }


}
