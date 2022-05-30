package it.leonardo.leonardoapiboot.entity.form;

public class RegisterForm {
	private String username;
	private String email;
	private String password;
	private String nome;
	private String cognome;

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

	@Override
	public String toString() {
		return "RegisterForm{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", nome='" + nome + '\'' +
				", cognome='" + cognome + '\'' +
				'}';
	}

	public RegisterForm() {
	}
}
