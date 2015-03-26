package serverclient;

/**
 * Created by Hope on 3/24/2015.
 */
public class Message {
    private Integer id;
    private String userName;
    private String text;

    public Message(String userName, String text) {
        this.userName = userName;
        this.text = text;
        id = (int)(Math.random() * 10101);
    }

    public Message(Integer id, String userName, String text) {
        this.id = id;
        this.userName = userName;
        this.text = text;
    }

    public int getId() {
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


}
