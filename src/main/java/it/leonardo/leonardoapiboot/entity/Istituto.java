package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "istituti")
public class Istituto implements Serializable {

    @Id
    @Column(name = "istituto_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer istitutoId;

    @Column(name = "codice_identificativo", length = 10)
    private String codiceIdentificativo;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "citta")
    private Citta citta;

    @JsonManagedReference
    @OneToMany(mappedBy = "istituto")
    private Set<Utente> utente;

    @Column(name = "path_logo")
    private String pathLogo;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lng")
    private Float lng;



    public Integer getIstitutoId() {
        return istitutoId;
    }

    public void setIstitutoId(Integer istitutoId) {
        this.istitutoId = istitutoId;
    }

    public String getCodiceIdentificativo() {
        return codiceIdentificativo;
    }

    public void setCodiceIdentificativo(String codiceIdentificativo) {
        this.codiceIdentificativo = codiceIdentificativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Citta getCitta() {
        return citta;
    }

    public void setCitta(Citta citta) {
        this.citta = citta;
    }

    public String getPathLogo() {
        return pathLogo;
    }

    public void setPathLogo(String pathLogo) {
        this.pathLogo = pathLogo;
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

    public Set<Utente> getUtenti() {
        return utente;
    }

    public void setUtenti(Set<Utente> utente) {
        this.utente = utente;
    }

    public Istituto() {
    }
}
