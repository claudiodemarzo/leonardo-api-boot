package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "utenti_preferences")
@Immutable
public class UtentiPreferences implements Serializable {

	@Id
	@Column(name = "utente_id")
	@JsonIgnore
	private Integer utenteId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "utente_id")
	@JsonBackReference
	private Utente utente;

	@Column(name = "preferences")
	private String preferences;

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

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public UtentiPreferences() {
	}
}

