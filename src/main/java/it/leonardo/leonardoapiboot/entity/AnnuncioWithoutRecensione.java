package it.leonardo.leonardoapiboot.entity;


import javax.persistence.*;

@Entity
@Table(name = "annunci_without_recensione")
public class AnnuncioWithoutRecensione {

    @Id
    @Column(name = "annuncio_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer annuncioId;

    @Column(name = "utente")
    private Integer utente;

    @Column(name = "sold_to")
    private Integer sold_to;

    @Column(name = "recensione_id")
    private Integer recensione_id;

    public Integer getAnnuncioId() {
        return annuncioId;
    }

    public void setAnnuncioId(Integer annuncioId) {
        this.annuncioId = annuncioId;
    }

    public Integer getUtente() {
        return utente;
    }

    public void setUtente(Integer utente) {
        this.utente = utente;
    }

    public Integer getSold_to() {
        return sold_to;
    }

    public void setSold_to(Integer sold_to) {
        this.sold_to = sold_to;
    }

    public Integer getRecensione_id() {
        return recensione_id;
    }

    public void setRecensione_id(Integer recensione_id) {
        this.recensione_id = recensione_id;
    }

    public AnnuncioWithoutRecensione() {
    }
}
