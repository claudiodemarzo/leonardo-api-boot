package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "citta")
public class Citta implements Serializable {

    @Id
    @Column(name = "citta_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cittaId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lng")
    private Float lng;

    @ManyToOne
    @JoinColumn(name = "provincia")
    private Provincia provincia;

    @Column(name = "cap", length = 5)
    private String cap;

    @JsonIgnore
    @OneToMany(mappedBy = "citta")
    private Set<Istituto> istituti;

    @JsonIgnore
    @OneToMany(mappedBy = "citta")
    private Set<AnnunciLibri> annunci;

    @JsonIgnore
    @OneToMany(mappedBy = "citta")
    private Set<Utente> utenti;

    public Integer getCittaId() {
        return cittaId;
    }

    public void setCittaId(Integer cittaId) {
        this.cittaId = cittaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public Set<Istituto> getIstituti() {
        return istituti;
    }

    public void setIstituti(Set<Istituto> istituti) {
        this.istituti = istituti;
    }

    public Set<AnnunciLibri> getAnnunci() {
        return annunci;
    }

    public void setAnnunci(Set<AnnunciLibri> annunci) {
        this.annunci = annunci;
    }

    public Set<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    public Citta() {
    }
}
