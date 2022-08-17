package it.leonardo.leonardoapiboot.entity;


import javax.persistence.*;

@Entity
@Table(name = "livelli")
public class Livello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "livello_id")
    private Integer livelloId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "soglia")
    private Integer soglia;

    public Integer getLivelloId() {
        return livelloId;
    }

    public void setLivelloId(Integer livelloId) {
        this.livelloId = livelloId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSoglia() {
        return soglia;
    }

    public void setSoglia(Integer soglia) {
        this.soglia = soglia;
    }

    public Livello() {
    }
}
