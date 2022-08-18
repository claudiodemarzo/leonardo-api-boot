package it.leonardo.leonardoapiboot.entity;

import org.json.JSONObject;

public class Notifica {
    public enum TipoNotifica {
        info,
        success,
        warning,
        error
    }

    private TipoNotifica type;
    private String title;
    private String content;

    public Notifica(TipoNotifica type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public TipoNotifica getType() {
        return type;
    }

    public void setType(TipoNotifica type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("type", type.toString());
        obj.put("title", title);
        obj.put("content", content);
        return obj.toString();
    }
}
