package it.leonardo.leonardoapiboot.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "recensioni")
public class Recensione implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recensione_id")
	private Integer recensioneId;

	@JsonBackReference
	@JoinColumn(name = "utente_recensito")
	@ManyToOne
	private Utente utenteRecensito;

	@OneToOne
	@JoinColumn(name = "annuncio")
	@JsonIgnore
	private AnnunciLibri annuncio;

	@JsonBackReference
	@JoinColumn(name = "utente_recensore")
	@ManyToOne
	private Utente utenteRecensore;

	@Column(name = "voto")
	private Integer voto;

	@Column(name = "commento")
	private String commento;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public Integer getRecensioneId() {
		return recensioneId;
	}

	public void setRecensioneId(Integer recensioneId) {
		this.recensioneId = recensioneId;
	}

	public Utente getUtenteRecensito() {
		return utenteRecensito;
	}

	public void setUtenteRecensito(Utente utenteRecensito) {
		this.utenteRecensito = utenteRecensito;
	}

	public Utente getUtenteRecensore() {
		return utenteRecensore;
	}

	public void setUtenteRecensore(Utente utenteRecensore) {
		this.utenteRecensore = utenteRecensore;
	}

	public Integer getVoto() {
		return voto;
	}

	public void setVoto(Integer voto) {
		this.voto = voto;
	}

	public AnnunciLibri getAnnuncio() {
		return annuncio;
	}

	public void setAnnuncio(AnnunciLibri annuncio) {
		this.annuncio = annuncio;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Recensione(){}
}
