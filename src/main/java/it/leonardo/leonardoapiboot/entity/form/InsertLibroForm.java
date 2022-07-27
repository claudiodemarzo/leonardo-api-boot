package it.leonardo.leonardoapiboot.entity.form;

public class InsertLibroForm {
    private String isbn;
    private String titolo;
    private String autori;
    private String editore;
    private String categoria;
    private String anno;
    private String descrizione;
    private Integer npag;

    private Float prezzo;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutori() {
        return autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getNpag() {
        return npag;
    }

    public void setNpag(Integer npag) {
        this.npag = npag;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public InsertLibroForm() {
    }

    @Override
    public String toString() {
        return "InsertLibroForm{" +
                "isbn='" + isbn + '\'' +
                ", titolo='" + titolo + '\'' +
                ", autori='" + autori + '\'' +
                ", editore='" + editore + '\'' +
                ", categoria='" + categoria + '\'' +
                ", anno='" + anno + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", npag=" + npag +
                '}';
    }
}
