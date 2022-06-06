package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "utenti_public_information")
public class UtentePublicInfo {

    @Id
    @Column(name = "utente_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private String bio;

    @Column
    private String nome;

    @Column
    private String cognome;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn
    private Istituto istituto;

    @Column
    private String moreinfo;

    @Column
    private Integer punti;

    @Column
    private String foto;

    @Column
    private String genere;

    @Column
    private String cap;

    @JoinColumn
    @JsonManagedReference
    @ManyToOne
    private Citta citta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Istituto getIstituto() {
        return istituto;
    }

    public void setIstituto(Istituto istituto) {
        this.istituto = istituto;
    }

    public String getMoreinfo() {
        return moreinfo;
    }

    public void setMoreinfo(String moreinfo) {
        this.moreinfo = moreinfo;
    }

    public Integer getPunti() {
        return punti;
    }

    public void setPunti(Integer punti) {
        this.punti = punti;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public Citta getCitta() {
        return citta;
    }

    public void setCitta(Citta citta) {
        this.citta = citta;
    }

    public UtentePublicInfo() {
    }
}
