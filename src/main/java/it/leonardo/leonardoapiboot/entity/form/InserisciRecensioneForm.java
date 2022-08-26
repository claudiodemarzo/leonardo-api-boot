package it.leonardo.leonardoapiboot.entity.form;

public class InserisciRecensioneForm {
    private Integer voto;
    private String commento;

    public Integer getVoto() {
        return voto;
    }

    public void setVoto(Integer voto) {
        this.voto = voto;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    @Override
    public String toString() {
        return "InserisciRecensioneForm{" +
                "voto=" + voto +
                ", commento='" + commento + '\'' +
                '}';
    }

    public InserisciRecensioneForm() {
    }
}
