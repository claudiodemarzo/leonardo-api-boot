package it.leonardo.leonardoapiboot.entity.form;

import java.util.Date;

public class MessaggioWS {
    private String messaggio;
    private Date timestamp;
    private Integer utenteDest;
    private String tipo;

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUtenteDest() {
        return utenteDest;
    }

    public void setUtenteDest(Integer utenteDest) {
        this.utenteDest = utenteDest;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "MessaggioWS{" +
                "messaggio='" + messaggio + '\'' +
                ", timestamp=" + timestamp +
                ", utenteDest=" + utenteDest +
                ", tipo=" + tipo +
                '}';
    }

    public MessaggioWS() {
    }

    public enum TipoMessaggio {
        text,
        image,
        location,
        ad,
        request
    }
}
