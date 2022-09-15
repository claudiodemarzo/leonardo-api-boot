package it.leonardo.leonardoapiboot.entity.form;

public class UpdatePublicForm {

    private String username;
    private String nome;
    private String cognome;
    private String bio;
    private Integer istituto;
    private String moreInfo;
    private String igUsername;
    private String ttUsername;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getIstituto() {
        return istituto;
    }

    public void setIstituto(Integer istituto) {
        this.istituto = istituto;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getIgUsername() {
        return igUsername;
    }

    public void setIgUsername(String igUsername) {
        this.igUsername = igUsername;
    }

    public String getTtUsername() {
        return ttUsername;
    }

    public void setTtUsername(String ttUsername) {
        this.ttUsername = ttUsername;
    }

    public UpdatePublicForm() {
    }
}
