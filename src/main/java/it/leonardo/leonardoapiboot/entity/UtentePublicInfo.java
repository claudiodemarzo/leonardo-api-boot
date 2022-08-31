package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "utenti_public_information")
public class UtentePublicInfo {

    @Id
    @Column(name = "utente_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "bio")
    private String bio;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "istituto")
    private Istituto istituto;

    @Column(name = "moreinfo")
    private String moreinfo;

    @Column(name = "punti")
    private Integer punti;

    @Column(name = "foto")
    private String foto;

    @Column(name = "genere")
    private String genere;

    @Column(name = "cap")
    private String cap;

    @JoinColumn(name = "citta")
    @JsonManagedReference
    @ManyToOne
    private Citta citta;

    @Column(name = "created_at")
    private Date createdAt;

    @Column("libri_venduti")
    private Integer libriVenduti;

    @Column(name = "online")
    private Boolean online;

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

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLibriVenduti() {
        return libriVenduti;
    }

    public void setLibriVenduti(Integer libriVenduti) {
        this.libriVenduti = libriVenduti;
    }

    public UtentePublicInfo() {
    }
}
