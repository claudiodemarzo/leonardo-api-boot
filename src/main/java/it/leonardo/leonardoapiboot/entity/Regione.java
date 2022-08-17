package it.leonardo.leonardoapiboot.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "regione")
public class Regione implements Serializable {

    @Id
    @Column(name = "regione_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regioneId;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "regione")
    private Set<Provincia> provincia;

    public Set<Provincia> getProvincia() {
        return provincia;
    }

    public void setProvincias(Set<Provincia> provincias) {
        this.provincia = provincias;
    }

    public int getRegioneId() {
        return regioneId;
    }

    public void setRegioneId(int regioneId) {
        this.regioneId = regioneId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Regione() {
    }
}
