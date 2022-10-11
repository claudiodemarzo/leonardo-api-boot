package it.leonardo.leonardoapiboot.entity.form;

public class SendEmbedForm {
    private String id;
    private String tipo;
    private String payload;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public SendEmbedForm(){
    }
}
