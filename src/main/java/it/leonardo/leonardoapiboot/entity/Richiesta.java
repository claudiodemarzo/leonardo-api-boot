package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.json.JSONObject;

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

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "messaggio")
    private Messaggio messaggio;

    @Column(name = "stato")
    private Integer stato;

    @Transient
    private Libro libro;

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

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("annuncio", annuncio);
        //json.put("messaggio", messaggio);
        json.put("stato", stato);
        json.put("libro", libro);
        return json.toString();
    }

    public JSONObject JSONObject(){
        return new JSONObject()
                .put("id", id)
                .put("annuncio", annuncio)
                .put("messaggio", messaggio)
                .put("stato", stato)
                .put("libro", libro);
    }

    public Richiesta() {
    }
}
