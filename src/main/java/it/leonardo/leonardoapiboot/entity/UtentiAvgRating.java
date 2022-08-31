package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "utenti_avg_rating")
@Immutable
public class UtentiAvgRating implements Serializable {

	@Id
	@Column(name = "utente_id")
	@JsonIgnore
	private Integer utenteId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "utente_id")
	@JsonBackReference
	private Utente utente;

	@OneToOne
	@MapsId
	@JoinColumn(name = "utente_id")
	@JsonBackReference
	private UtentePublicInfo utentePublicInfo;

	@Column(name = "avg_voto")
	private Float avgVoto;

	@Column(name = "nvoti")
	private Integer numeroVoti;

	public Integer getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(Integer utenteId) {
		this.utenteId = utenteId;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Float getAvgVoto() {
		return avgVoto;
	}

	public void setAvgVoto(Float avgVoto) {
		this.avgVoto = avgVoto;
	}

	public Integer getNumeroVoti() {
		return numeroVoti;
	}

	public void setNumeroVoti(Integer numeroVoti) {
		this.numeroVoti = numeroVoti;
	}

	public UtentePublicInfo getUtentePublicInfo() {
		return utentePublicInfo;
	}

	public void setUtentePublicInfo(UtentePublicInfo utentePublicInfo) {
		this.utentePublicInfo = utentePublicInfo;
	}

	public UtentiAvgRating() {
	}
}
