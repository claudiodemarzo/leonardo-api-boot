package it.leonardo.leonardoapiboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "citta_simple")
public class CittaSimple {

    @Id
    @Column(name = "citta_id")
    private Integer cittaId;

    @Column(name = "nome")
    private String nome;

    public Integer getCittaId() {
        return cittaId;
    }

    public void setCittaId(Integer cittaId) {
        this.cittaId = cittaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CittaSimple() {
    }
}
