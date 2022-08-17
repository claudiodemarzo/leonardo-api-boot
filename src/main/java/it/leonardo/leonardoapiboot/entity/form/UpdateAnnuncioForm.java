package it.leonardo.leonardoapiboot.entity.form;

public class UpdateAnnuncioForm {

    private Integer id;
    private Float prezzoVendita;
    private Integer sottCanc;
    private Integer sottNonCanc;
    private Integer scrittCanc;
    private Integer scrittNonCanc;
    private Integer pagManc;
    private Integer pagRov;
    private Integer pagRovMol;
    private Integer copRov;
    private Integer insManc;

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
    public Float getPrezzoVendita() {
        return prezzoVendita;
    }

    public void setPrezzoVendita(Float prezzoVendita) {
        this.prezzoVendita = prezzoVendita;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UpdateAnnuncioForm{" +
                "id=" + id +
                ", prezzoVendita=" + prezzoVendita +
                ", sottCanc=" + sottCanc +
                ", sottNonCanc=" + sottNonCanc +
                ", scrittCanc=" + scrittCanc +
                ", scrittNonCanc=" + scrittNonCanc +
                ", pagManc=" + pagManc +
                ", pagRov=" + pagRov +
                ", pagRovMol=" + pagRovMol +
                ", copRov=" + copRov +
                ", insManc=" + insManc +
                '}';
    }

    public UpdateAnnuncioForm() {
    }
}
