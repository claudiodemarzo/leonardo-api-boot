package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

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

    @Column(name = "libri_venduti")
    private Integer libriVenduti;

    @Column(name = "online")
    private Boolean online;

    @Column(name = "ig_username")
    private String igUsername;

    @Column(name = "tt_username")
    private String ttUsername;

    @JsonManagedReference
    @OneToOne(mappedBy = "utentePublicInfo")
    @PrimaryKeyJoinColumn
    private UtentiAvgRating avgRating;

    @ManyToOne
    @JoinColumn(name = "activeTag")
    private Tag activeTag;

    @Transient
    private List<Recensione> recensioniRicevute;

    @Transient
    private List<Recensione> recensioniFatte;

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

    public UtentiAvgRating getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(UtentiAvgRating avgRating) {
        this.avgRating = avgRating;
    }

    public List<Recensione> getRecensioniRicevute() {
        return recensioniRicevute;
    }

    public void setRecensioniRicevute(List<Recensione> recensioniRicevute) {
        this.recensioniRicevute = recensioniRicevute;
    }

    public List<Recensione> getRecensioniFatte() {
        return recensioniFatte;
    }

    public void setRecensioniFatte(List<Recensione> recensioniFatte) {
        this.recensioniFatte = recensioniFatte;
    }

    public Tag getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(Tag activeTag) {
        this.activeTag = activeTag;
    }

    public String getIgUsername() {
        return igUsername;
    }

    public void setIgUsername(String igUsername) {
        this.igUsername = igUsername;
    }

    public String getTtUsername() {
        return ttUsername;
    }

    public void setTtUsername(String ttUsername) {
        this.ttUsername = ttUsername;
    }

    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("bio", bio);
        map.put("nome", nome);
        map.put("cognome", cognome);
        map.put("istituto", istituto);
        map.put("moreinfo", moreinfo);
        map.put("punti", punti);
        map.put("foto", foto);
        map.put("genere", genere);
        map.put("cap", cap);
        map.put("citta", citta);
        map.put("createdAt", createdAt);
        map.put("libriVenduti", libriVenduti);
        map.put("online", online);
        map.put("avgRating", avgRating);
        map.put("igUsername", igUsername);
        map.put("ttUsername", ttUsername);
        List<Recensione> rr = new ArrayList<>(), rf = new ArrayList<>();
        if (recensioniFatte != null && !recensioniFatte.isEmpty()) {
            recensioniFatte.forEach(r -> {
                Recensione recensione = new Recensione();
                recensione.setRecensioneId(r.getRecensioneId());
                UtentePublicInfo rpi = r.getUtenteRecensorePublicInfo();
                rpi.setRecensioniFatte(null);
                rpi.setRecensioniRicevute(null);
                recensione.setUtenteRecensorePublicInfo(rpi);
                UtentePublicInfo rpi2 = r.getUtenteRecensitoPublicInfo();
                rpi2.setRecensioniFatte(null);
                rpi2.setRecensioniRicevute(null);
                recensione.setUtenteRecensitoPublicInfo(rpi2);
                recensione.setUtenteRecensito(r.getUtenteRecensito());
                recensione.setVoto(r.getVoto());
                recensione.setAnnuncio(r.getAnnuncio());
                recensione.setCreatedAt(r.getCreatedAt());
                recensione.setCommento(r.getCommento());
                recensione.setUtenteRecensore(r.getUtenteRecensore());
                rf.add(recensione);
            });
        }

        if (recensioniRicevute != null && !recensioniRicevute.isEmpty()) {
            recensioniRicevute.forEach(r -> {
                Recensione recensione = new Recensione();
                recensione.setRecensioneId(r.getRecensioneId());
                UtentePublicInfo rpi = r.getUtenteRecensorePublicInfo();
                rpi.setRecensioniFatte(null);
                rpi.setRecensioniRicevute(null);
                recensione.setUtenteRecensorePublicInfo(rpi);
                UtentePublicInfo rpi2 = r.getUtenteRecensitoPublicInfo();
                rpi2.setRecensioniFatte(null);
                rpi2.setRecensioniRicevute(null);
                recensione.setUtenteRecensitoPublicInfo(rpi2);
                recensione.setUtenteRecensito(r.getUtenteRecensito());
                recensione.setVoto(r.getVoto());
                recensione.setAnnuncio(r.getAnnuncio());
                recensione.setCreatedAt(r.getCreatedAt());
                recensione.setCommento(r.getCommento());
                recensione.setUtenteRecensore(r.getUtenteRecensore());
                rr.add(recensione);
            });
        }
        map.put("recensioniRicevute", rr);
        map.put("recensioniFatte", rf);
        map.put("activeTag", activeTag);
        return map;
    }

    public UtentePublicInfo() {
    }
}
