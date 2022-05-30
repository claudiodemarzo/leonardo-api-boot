package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "carrello")
public class Carrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "entry_id")
	private Integer entryId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "utente")
	private Utente utente;

	@ManyToOne
	@JoinColumn(name = "annuncio")
	private AnnunciLibri annuncio;

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public AnnunciLibri getAnnuncio() {
		return annuncio;
	}

	public void setAnnuncio(AnnunciLibri annuncio) {
		this.annuncio = annuncio;
	}

	public Carrello() {
	}
}
