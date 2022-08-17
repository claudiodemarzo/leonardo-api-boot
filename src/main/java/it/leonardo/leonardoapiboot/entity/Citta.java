package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "citta")
public class Citta implements Serializable {

    @Id
    @Column(name = "citta_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cittaId;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "provincia")
    @JsonManagedReference
    private Provincia provincia;

    @JsonManagedReference
    @OneToMany(mappedBy = "citta", fetch = FetchType.EAGER)
    private List<CAP> caps;

    @JsonBackReference
    @OneToMany(mappedBy = "citta")
    private Set<Istituto> istituti;

    @JsonBackReference
    @OneToMany(mappedBy = "citta")
    private Set<Utente> utenti;

    @JsonBackReference
    @OneToMany(mappedBy = "citta")
    private List<UtentePublicInfo> utentePublicInfos;

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

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Set<Istituto> getIstituti() {
        return istituti;
    }

    public void setIstituti(Set<Istituto> istituti) {
        this.istituti = istituti;
    }

    public Set<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    public List<UtentePublicInfo> getUtentePublicInfos() {
        return utentePublicInfos;
    }

    public void setUtentePublicInfos(List<UtentePublicInfo> utentePublicInfos) {
        this.utentePublicInfos = utentePublicInfos;
    }

    public List<CAP> getCaps() {
        return caps;
    }

    public void setCaps (List<CAP> caps) {
        this.caps = caps;
    }

    public Citta() {
    }
}
