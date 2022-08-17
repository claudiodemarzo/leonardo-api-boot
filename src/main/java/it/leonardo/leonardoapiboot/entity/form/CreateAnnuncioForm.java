package it.leonardo.leonardoapiboot.entity.form;

public class CreateAnnuncioForm {
    private String isbn;
    private String titolo;
    private String autore;
    private String casaed;
    private String categoria;
    private String anno;
    private Float prezzoListino;
    private Float prezzoVendita;
    private Integer numeroPagine;
    private String descrizione;
    private Integer sottCanc;
    private Integer sottNonCanc;
    private Integer scrittCanc;
    private Integer scrittNonCanc;
    private Integer pagManc;
    private Integer pagRov;
    private Integer pagRovMol;
    private Integer copRov;
    private Integer insManc;

    private String imgLink;

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

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getCasaed() {
        return casaed;
    }

    public void setCasaed(String casaed) {
        this.casaed = casaed;
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

    public Integer getNumeroPagine() {
        return numeroPagine;
    }

    public void setNumeroPagine(Integer numeroPagine) {
        this.numeroPagine = numeroPagine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getSottCanc() {
        return sottCanc;
    }

    public void setSottCanc(Integer sottCanc) {
        this.sottCanc = sottCanc;
    }

    public Integer getSottNonCanc() {
        return sottNonCanc;
    }

    public void setSottNonCanc(Integer sottNonCanc) {
        this.sottNonCanc = sottNonCanc;
    }

    public Integer getScrittCanc() {
        return scrittCanc;
    }

    public void setScrittCanc(Integer scrittCanc) {
        this.scrittCanc = scrittCanc;
    }

    public Integer getScrittNonCanc() {
        return scrittNonCanc;
    }

    public void setScrittNonCanc(Integer scrittNonCanc) {
        this.scrittNonCanc = scrittNonCanc;
    }

    public Integer getPagManc() {
        return pagManc;
    }

    public void setPagManc(Integer pagManc) {
        this.pagManc = pagManc;
    }

    public Integer getPagRov() {
        return pagRov;
    }

    public void setPagRov(Integer pagRov) {
        this.pagRov = pagRov;
    }

    public Integer getPagRovMol() {
        return pagRovMol;
    }

    public void setPagRovMol(Integer pagRovMol) {
        this.pagRovMol = pagRovMol;
    }

    public Integer getCopRov() {
        return copRov;
    }

    public void setCopRov(Integer copRov) {
        this.copRov = copRov;
    }

    public Integer getInsManc() {
        return insManc;
    }

    public void setInsManc(Integer insManc) {
        this.insManc = insManc;
    }

    public Float getPrezzoListino() {
        return prezzoListino;
    }

    public void setPrezzoListino(Float prezzoListino) {
        this.prezzoListino = prezzoListino;
    }

    public Float getPrezzoVendita() {
        return prezzoVendita;
    }

    public void setPrezzoVendita(Float prezzoVendita) {
        this.prezzoVendita = prezzoVendita;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    @Override
    public String toString() {
        return "CreateAnnuncioForm{" +
                "isbn='" + isbn + '\'' +
                ", titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", casaed='" + casaed + '\'' +
                ", categoria='" + categoria + '\'' +
                ", anno='" + anno + '\'' +
                ", numeroPagine=" + numeroPagine +
                ", descrizione='" + descrizione + '\'' +
                ", sottCanc=" + sottCanc +
                ", sottNonCanc=" + sottNonCanc +
                ", scrittCanc=" + scrittCanc +
                ", scrittNonCanc=" + scrittNonCanc +
                ", pagManc=" + pagManc +
                ", pagRov=" + pagRov +
                ", pagRovMol=" + pagRovMol +
                ", copRov=" + copRov +
                ", insManc=" + insManc +
                ", imgLink=\'" + imgLink + '\'' +
                '}';
    }

    public CreateAnnuncioForm() {
    }
}
