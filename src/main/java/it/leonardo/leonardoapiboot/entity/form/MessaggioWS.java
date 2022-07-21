package it.leonardo.leonardoapiboot.entity.form;

import java.util.Date;

public class MessaggioWS {
    private String messaggio;
    private Date timestamp;
    private Integer utenteDest;

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

    public MessaggioWS() {
    }
}
