package it.leonardo.leonardoapiboot.entity.form;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class UpdatePrivateForm {
	private String email;
	private String genere;

	@Temporal(TemporalType.DATE)
	private String dataNascita;
	private String telefono;
	private Integer citta;
	private String cap;
	private String indirizzo;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Integer getCitta() {
		return citta;
	}

	public void setCitta(Integer citta) {
		this.citta = citta;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	@Override
	public String toString() {
		return "UpdatePrivateForm{" +
				"email='" + email + '\'' +
				", genere='" + genere + '\'' +
				", dataNascita=" + dataNascita +
				", telefono='" + telefono + '\'' +
				", citta=" + citta +
				", cap='" + cap + '\'' +
				", indirizzo='" + indirizzo + '\'' +
				'}';
	}

	public UpdatePrivateForm() {
	}
}
