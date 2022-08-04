package it.leonardo.leonardoapiboot.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "annunci_libri")
public class AnnunciLibri{

	@Id
	@Column(name = "annuncio_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	@JoinColumn(name = "citta")
	private Citta citta;

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

	public Citta getCitta() {
		return citta;
	}

	public void setCitta(Citta citta) {
		this.citta = citta;
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
}
