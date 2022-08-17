package it.leonardo.leonardoapiboot.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

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

	@JsonBackReference
	@JoinColumn(name = "utente_recensore")
	@ManyToOne
	private Utente utenteRecensore;

	@Column(name = "voto")
	private Integer voto;

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

	public Recensione(){}
}
