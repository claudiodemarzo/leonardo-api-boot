package it.leonardo.leonardoapiboot.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "status_libri")
public class StatusLibro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer id;

    @Column(name = "sott_canc")
    private Integer sottCanc;

    @Column(name = "sott_noncanc")
    private Integer sottNonCanc;

    @Column(name = "scritt_canc")
    private Integer scrittCanc;

    @Column(name = "scritt_noncanc")
    private Integer scrittNonCanc;

    @Column(name = "pag_manc")
    private Integer pagManc;

    @Column(name = "pag_rov")
    private Integer pagRov;

    @Column(name = "pag_rov_mol")
    private Integer pagRovMol;

    @Column(name = "cop_rov")
    private Integer copRov;

    @Column(name = "ins_manc")
    private Integer insManc;

    @JsonBackReference
    @OneToMany(mappedBy = "status")
    private List<AnnunciLibri> annunci;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<AnnunciLibri> getAnnunci() {
        return annunci;
    }

    public void setAnnuncio(List<AnnunciLibri> annunci) {
        this.annunci = annunci;
    }

    @Override
    public String toString() {
        return "StatusLibro{" +
                "id=" + id +
                ", sottCanc=" + sottCanc +
                ", sottNonCanc=" + sottNonCanc +
                ", scrittCanc=" + scrittCanc +
                ", scrittNonCanc=" + scrittNonCanc +
                ", pagManc=" + pagManc +
                ", pagRov=" + pagRov +
                ", pagRovMol=" + pagRovMol +
                ", copRov=" + copRov +
                ", insManc=" + insManc +
                ", annunci=" + annunci +
                '}';
    }

    public StatusLibro(){
    }


}
