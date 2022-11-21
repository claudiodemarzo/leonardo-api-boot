package it.leonardo.leonardoapiboot.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.leonardo.leonardoapiboot.entity.form.CreateAnnuncioForm;
import it.leonardo.leonardoapiboot.entity.form.UpdateAnnuncioForm;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "annunci_libri")
public class AnnunciLibri {

    @Id
    @Column(name = "annuncio_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer annuncio_id;

    @Column(name = "prezzo")
    private Float prezzo;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Column(name = "stato")
    private Integer stato;

    @Column(name = "livello_usura")
    private Character livello_usura;

    @ManyToOne
    @JoinColumn(name = "utente")
    private UtentePublicInfo utente;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "isbn")
    private Libro libro;

    @JsonIgnore
    @OneToMany(mappedBy = "annuncio")
    private Set<Carrello> carrello;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "status_libro")
    private StatusLibro status;

    @ManyToOne
    @JoinColumn(name = "soldTo")
    private UtentePublicInfo soldTo;

    @Column(name = "sale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;

    @OneToOne(mappedBy = "annuncio")
    private Recensione recensione;

    @JsonBackReference
    @OneToMany(mappedBy = "annuncio")
    private List<Richiesta> richieste;

    public Integer getAnnuncio_id() {
        return annuncio_id;
    }

    public void setAnnuncio_id(Integer annuncio_id) {
        this.annuncio_id = annuncio_id;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Integer getStato() {
        return stato;
    }

    public void setStato(Integer stato) {
        this.stato = stato;
    }

    public Character getLivello_usura() {
        return livello_usura;
    }

    public void setLivello_usura(Character livello_usura) {
        this.livello_usura = livello_usura;
    }

    public UtentePublicInfo getUtente() {
        return utente;
    }

    public void setUtente(UtentePublicInfo utente) {
        this.utente = utente;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Set<Carrello> getCarrello() {
        return carrello;
    }

    public void setCarrello(Set<Carrello> carrello) {
        this.carrello = carrello;
    }

    public StatusLibro getStatus() {
        return status;
    }

    public void setStatus(StatusLibro status) {
        this.status = status;
    }

    public UtentePublicInfo getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(UtentePublicInfo soldTo) {
        this.soldTo = soldTo;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Recensione getRecensione() {
        return recensione;
    }

    public void setRecensione(Recensione recensione) {
        this.recensione = recensione;
    }

    public List<Richiesta> getRichieste() {
        return richieste;
    }

    public void setRichieste(List<Richiesta> richieste) {
        this.richieste = richieste;
    }

    public AnnunciLibri() {
    }

    @Override
    public String toString() {
        return "AnnunciLibri{" +
                "annuncio_id=" + annuncio_id +
                ", prezzo=" + prezzo +
                ", created_at=" + created_at +
                ", livello_usura=" + livello_usura +
                '}';
    }

    public static AnnunciLibri fromCreateAnnuncioForm(Libro l, UtentePublicInfo upf, StatusLibro sl, CreateAnnuncioForm caf) {
        AnnunciLibri al = new AnnunciLibri();
        al.setLibro(l);
        al.setPrezzo(caf.getPrezzoVendita());
        al.setStato(1);
        al.setLivello_usura(AnnunciLibri.calcolaLivelloUsura(caf.getSottCanc(), caf.getSottNonCanc(), caf.getScrittCanc(), caf.getScrittNonCanc(), caf.getPagManc(), caf.getPagRov(), caf.getPagRovMol(), caf.getCopRov(), caf.getInsManc()));
        al.setUtente(upf);
        al.setStatus(sl);
        return al;
    }

    public static char calcolaLivelloUsura(UpdateAnnuncioForm form) {
        return calcolaLivelloUsura(form.getSottCanc(), form.getSottNonCanc(), form.getScrittCanc(), form.getScrittNonCanc(), form.getPagManc(), form.getPagRov(), form.getPagRovMol(), form.getCopRov(), form.getInsManc());
    }

    public static char calcolaLivelloUsura(Integer sottCanc, Integer sottNonCanc, Integer scrittCanc, Integer scrittNonCanc, Integer pagManc, Integer pagRov, Integer pagRovMol, Integer copRov, Integer insManc) {
        int sum = 0;

        switch (sottCanc) {
            case 0:
                break;
            case 1:
                sum += 1;
                break;
            case 2:
                sum += 2;
                break;
            case 3:
                sum += 3;
                break;
        }

        switch (sottNonCanc) {
            case 0:
                break;
            case 1:
                sum += 2;
                break;
            case 2:
                sum += 3;
                break;
            case 3:
                sum += 5;
                break;
        }

        switch (scrittCanc) {
            case 0:
                break;
            case 1:
                sum += 1;
                break;
            case 2:
                sum += 3;
                break;
            case 3:
                sum += 4;
                break;
        }

        switch (scrittNonCanc) {
            case 0:
                break;
            case 1:
                sum += 3;
                break;
            case 2:
                sum += 4;
                break;
            case 3:
                sum += 6;
                break;
        }

        switch (pagManc) {
            case 0:
                break;
            case 1:
                sum += 3;
                break;
            case 2:
                sum += 6;
                break;
            case 3:
                sum += 9;
                break;
        }

        switch (pagRov) {
            case 0:
                break;
            case 1:
                sum += 1;
                break;
            case 2:
                sum += 2;
                break;
            case 3:
                sum += 4;
                break;
        }

        switch (pagRovMol) {
            case 0:
                break;
            case 1:
                sum += 3;
                break;
            case 2:
                sum += 4;
                break;
            case 3:
                sum += 6;
                break;
        }

        switch (copRov) {
            case 0:
                break;
            case 1:
                sum += 1;
                break;
            case 2:
                sum += 2;
                break;
            case 3:
                sum += 3;
                break;
        }

        switch (insManc) {
            case 0:
                break;
            case 1:
            case 2:
                sum += 2;
                break;
        }

        if (sum <= 1) return 'n';
        if (sum <= 4) return 'o';
        if (sum <= 8) return 'b';
        if (sum <= 13) return 'd';
        if (sum <= 18) return 'a';
        return 'x';
    }

    public void copyFromUpdateAnnuncioForm(UpdateAnnuncioForm form, StatusLibro sl) {
        this.setPrezzo(form.getPrezzoVendita());
        this.setLivello_usura(AnnunciLibri.calcolaLivelloUsura(form));
        this.setStatus(sl);
    }

    public String toJSONString(){
        return new JSONObject()
                .put("annuncio_id", this.getAnnuncio_id())
                .put("prezzo", this.getPrezzo())
                .put("created_at", this.getCreated_at())
                .put("livello_usura", this.getLivello_usura())
                .put("libro", this.getLibro().getLibroId())
                .put("utente", this.getUtente().getId())
                .put("status", this.getStatus().getId())
                .put("stato", this.getStato())
                .put("saleDate", this.getSaleDate())
                .toString();
    }
}
