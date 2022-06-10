package it.leonardo.leonardoapiboot.entity;


import com.fasterxml.jackson.annotation.*;
import it.leonardo.leonardoapiboot.entity.form.RegisterForm;
import it.leonardo.leonardoapiboot.entity.form.UpdatePrivateForm;
import it.leonardo.leonardoapiboot.entity.form.UpdatePublicForm;
import org.json.JSONPropertyName;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "utenti")
public class Utente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "utente_id")
	private Integer utenteId;

	@Column(name = "created_at")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern="yyyy-MM-dd", timezone = "Europe/Rome")
	private Date createdAt = Calendar.getInstance().getTime();

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	@Column(name = "telefono", length = 10)
	private String telefono;

	@Column(name = "bio")
	private String bio;

	@Column(name = "nome")
	private String nome;

	@Column(name = "cognome")
	private String cognome;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "istituto")
	private Istituto istituto;

	@Column(name = "moreinfo")
	private String moreInfo;

	@Column(name = "indirizzo")
	private String indirizzo;

	@Column(name = "punti")
	private Integer punti = 0;

	@Column(name = "foto")
	private String foto;

	@Column(name = "datanascita")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern="yyyy-MM-dd", timezone = "Europe/Rome")
	private Date dataNascita;

	@Column(name = "genere")
	private String genere;

	@Column(name = "cap")
	private String cap;

	@Column(name = "email_confermata")
	private Boolean email_confermata = false;

	@JsonIgnore
	@Column(name = "reset_token")
	private String resetToken;

	@JsonIgnore
	@Column(name = "confirm_token")
	private String confirmToken;

	@JsonManagedReference
	@OneToMany(mappedBy = "utente")
	private List<TagsUtente> tags;

	@JsonBackReference
	@OneToMany(mappedBy = "utente")
	private Set<AnnunciLibri> annunciLibri;

	@JsonBackReference
	@OneToMany(mappedBy = "utente")
	private Set<Carrello> carrello;

	@JsonBackReference
	@OneToMany(mappedBy = "utenteRecensito")
	private List<Recensione> recensioniRicevute;

	@JsonBackReference
	@OneToMany(mappedBy = "utenteRecensore")
	private List<Recensione> recensioniFatte;

	@JsonManagedReference
	@OneToOne(mappedBy = "utente")
	@PrimaryKeyJoinColumn
	private UtentiAvgRating avgRating;

	@JoinColumn(name = "citta")
	@JsonManagedReference
	@ManyToOne
	private Citta citta;

	@OneToOne(mappedBy = "utente")
	@PrimaryKeyJoinColumn
	private UtentiPreferences preferences;

	public Integer getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(Integer utenteId) {
		this.utenteId = utenteId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
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

	public Istituto getIstituto() {
		return istituto;
	}

	public Set<Carrello> getCarrello() {
		return carrello;
	}

	public void setIstituto(Istituto istituto) {
		this.istituto = istituto;
	}

	public Integer getPunti() {
		return punti;
	}

	public void setPunti(Integer punti) {
		this.punti = punti;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Boolean getEmail_confermata() {
		return email_confermata;
	}

	public void setEmail_confermata(Boolean email_confermata) {
		this.email_confermata = email_confermata;
	}

	public Set<AnnunciLibri> getAnnunciLibri() {
		return annunciLibri;
	}

	public void setAnnunciLibri(Set<AnnunciLibri> annunciLibri) {
		this.annunciLibri = annunciLibri;
	}

	public void setCarrello(Set<Carrello> carrello) {
		this.carrello = carrello;
	}

	public List<TagsUtente> getTags() {
		return tags;
	}

	public void addTags(TagsUtente tag) {
		tags.add(tag);
	}

	public void setTags(List<TagsUtente> tags) {
		this.tags = tags;
	}

	public List<Recensione> getRecensioniRicevute() {
		return recensioniRicevute;
	}

	public void setRecensioniRicevute(List<Recensione> recensioniRicevute) {
		this.recensioniRicevute = recensioniRicevute;
	}

	public List<Recensione> getRecensioniFatte() {
		return recensioniFatte;
	}

	public void setRecensioniFatte(List<Recensione> recensioniFatte) {
		this.recensioniFatte = recensioniFatte;
	}

	public UtentiAvgRating getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(UtentiAvgRating avgRating) {
		this.avgRating = avgRating;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
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

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public Citta getCitta() {
		return citta;
	}

	public void setCitta(Citta citta) {
		this.citta = citta;
	}

	public UtentiPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(UtentiPreferences preferences) {
		this.preferences = preferences;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public String getConfirmToken() {
		return confirmToken;
	}

	public void setConfirmToken(String confirmToken) {
		this.confirmToken = confirmToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Utente utente = (Utente) o;
		return Objects.equals(getUtenteId(), utente.getUtenteId()) && Objects.equals(getCreatedAt(), utente.getCreatedAt()) && Objects.equals(getUsername(), utente.getUsername()) && Objects.equals(getEmail(), utente.getEmail()) && Objects.equals(getPassword(), utente.getPassword()) && Objects.equals(getTelefono(), utente.getTelefono()) && Objects.equals(getBio(), utente.getBio()) && Objects.equals(getNome(), utente.getNome()) && Objects.equals(getCognome(), utente.getCognome()) && Objects.equals(getIstituto(), utente.getIstituto()) && Objects.equals(getPunti(), utente.getPunti()) && Objects.equals(getFoto(), utente.getFoto()) && Objects.equals(getEmail_confermata(), utente.getEmail_confermata()) && Objects.equals(getTags(), utente.getTags()) && Objects.equals(getAnnunciLibri(), utente.getAnnunciLibri()) && Objects.equals(getCarrello(), utente.getCarrello()) && Objects.equals(getRecensioniRicevute(), utente.getRecensioniRicevute()) && Objects.equals(getRecensioniFatte(), utente.getRecensioniFatte());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUtenteId(), getCreatedAt(), getUsername(), getEmail(), getPassword(), getTelefono(), getBio(), getNome(), getCognome(), getIstituto(), getPunti(), getFoto(), getEmail_confermata(), getTags(), getAnnunciLibri(), getCarrello(), getRecensioniRicevute(), getRecensioniFatte());
	}

	public void copyFromRegisterForm(RegisterForm rf) throws ParseException {
		this.username = rf.getUsername();
		this.password = rf.getPassword();
		this.nome = rf.getNome();
		this.cognome = rf.getCognome();
		this.email = rf.getEmail();
		this.genere = rf.getGenere();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
		sdf.setLenient(true);
		this.dataNascita = sdf.parse(rf.getDataNascita());
	}

	public void copyFromPublicUpdateForm(UpdatePublicForm upf) {
		this.nome = upf.getNome();
		this.cognome = upf.getCognome();
		this.bio = upf.getBio();
		this.moreInfo = upf.getMoreInfo();
	}

	public void copyFromPrivateUpdateForm(UpdatePrivateForm upf) {
		if(!email.equals(upf.getEmail())){
			email_confermata = false;
		}
		this.email = upf.getEmail();
		this.genere = upf.getGenere();
		this.dataNascita = upf.getDataNascita();
		this.telefono = upf.getTelefono();
		this.cap = upf.getCap();
		this.indirizzo = upf.getIndirizzo();

	}

	public Utente() {
	}
}
