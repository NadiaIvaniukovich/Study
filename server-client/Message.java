package serverclient;

import org.json.simple.JSONObject;

/**
 * Created by Hope on 3/24/2015.
 */
public class Message {
    private Long id;
    private String userName;
    private String text;

    public Message(String userName, String text) {
        this.userName = userName;
        this.text = text;
        id = (long)(Math.random() * 10101);
    }

    public Message(Long id, String userName, String text) {
        this.id = id;
        this.userName = userName;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"user\":\"" + this.userName + "\",\"message\":\"" + this.text + "\"}";
    }

}
