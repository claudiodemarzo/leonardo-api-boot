package it.leonardo.leonardoapiboot.entity.form;

import java.util.Date;

public class RegisterForm {
	private String username;
	private String email;
	private String password;
	private String nome;
	private String cognome;
	private Date dataNascita;
	private String genere;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	@Override
	public String toString() {
		return "RegisterForm{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", nome='" + nome + '\'' +
				", cognome='" + cognome + '\'' +
				", dataNascita=" + dataNascita +
				", genere='" + genere + '\'' +
				'}';
	}

	public RegisterForm() {
	}
}
