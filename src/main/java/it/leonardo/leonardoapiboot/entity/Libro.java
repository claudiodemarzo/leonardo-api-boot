package it.leonardo.leonardoapiboot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Year;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "libri")
public class Libro implements Serializable {

    @Id
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "libro_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer libroId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "copertina")
    private String copertina;

    @Column(name = "autori")
    private String autori;

    @Column(name = "casaed")
    private String casaed;

    @Column(name = "pagine")
    private int pagine;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "prezzolistino")
    private float prezzolistino;

    @Column(name = "anno_pubblicazione")
    private String anno_pubblicazione;

    @JsonManagedReference
    @OneToMany(mappedBy = "libro")
    private List<AnnunciLibri> annunci;

    public static Libro fromJSON(JSONObject json) {
        Libro l = new Libro();
        JSONObject volumeInfo = json.getJSONObject("volumeInfo");
        JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
        for (int i = 0; i < industryIdentifiers.length(); i++) {
            JSONObject id = industryIdentifiers.getJSONObject(i);
            if (id.getString("type").equals("ISBN_13")) {
                l.setIsbn(id.getString("identifier"));
            }
        }
        l.setNome(volumeInfo.getString("title") + " " + (volumeInfo.has("subtitle") ? ": " + volumeInfo.getString("subtitle") : ""));
        l.setDescrizione(volumeInfo.has("description") ? volumeInfo.getString("description") : "");
        l.setCopertina(volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : "");
        l.setAutori(volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").join(", ") : "");
        l.setCasaed(volumeInfo.has("publisher") ? volumeInfo.getJSONObject("publisher").getString("name") : "");
        l.setPagine(volumeInfo.has("pageCount") ? volumeInfo.getInt("pageCount"): 0);
        l.setCategoria(volumeInfo.has("categories") ? volumeInfo.getJSONArray("categories").join(", ") : "");
        l.setPrezzolistino((json.has("saleInfo") && json.getJSONObject("saleInfo").has("listPrice") ? json.getJSONObject("saleInfo").getJSONObject("listPrice").getFloat("amount") : 0));
        l.setAnno_pubblicazione(volumeInfo.has("publishedDate") ? volumeInfo.getString("publishedDate").substring(0, 4) : "0000");
        return l;
    }

    public String getAnno_pubblicazione() {
        return anno_pubblicazione;
    }

    public void setAnno_pubblicazione(String anno_pubblicazione) {
        this.anno_pubblicazione = anno_pubblicazione;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCopertina() {
        return copertina;
    }

    public void setCopertina(String copertina) {
        this.copertina = copertina;
    }

    public String getAutori() {
        return autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public String getCasaed() {
        return casaed;
    }

    public void setCasaed(String casaed) {
        this.casaed = casaed;
    }

    public int getPagine() {
        return pagine;
    }

    public void setPagine(int pagine) {
        this.pagine = pagine;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getPrezzolistino() {
        return prezzolistino;
    }

    public void setPrezzolistino(float prezzolistino) {
        this.prezzolistino = prezzolistino;
    }

    public List<AnnunciLibri> getAnnunci() {
        return annunci;
    }

    public void setAnnunci(List<AnnunciLibri> annunci) {
        this.annunci = annunci;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return pagine == libro.pagine && Float.compare(libro.prezzolistino, prezzolistino) == 0 && Objects.equals(isbn, libro.isbn) && Objects.equals(nome, libro.nome) && Objects.equals(descrizione, libro.descrizione) && Objects.equals(copertina, libro.copertina) && Objects.equals(autori, libro.autori) && Objects.equals(casaed, libro.casaed) && Objects.equals(categoria, libro.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, nome, descrizione, copertina, autori, casaed, pagine, categoria, prezzolistino);
    }

    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
    }
}
