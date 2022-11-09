package it.leonardo.leonardoapiboot.entity;

import javax.persistence.*;

@Entity
@Table(name = "richieste")
public class Richiesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "richiesta_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "annuncio")
    private AnnunciLibri annuncio;

    @OneToOne(mappedBy = "richiesta")
    @JoinColumn(name = "messaggio")
    private Messaggio messaggio;

    @Column(name = "stato")
    private Integer stato;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnnunciLibri getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(AnnunciLibri annuncio) {
        this.annuncio = annuncio;
    }

    public Messaggio getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(Messaggio messaggio) {
        this.messaggio = messaggio;
    }

    public Integer getStato() {
        return stato;
    }

    public void setStato(Integer stato) {
        this.stato = stato;
    }

    public Richiesta() {
    }
}
