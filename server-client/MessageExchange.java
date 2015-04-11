package serverclient;

/**
 * Created by Hope on 3/14/2015.
 */
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MessageExchange {

    private JSONParser jsonParser = new JSONParser();

    public String getToken(int index) {
        Integer number = index * 8 + 11;
        return "TN" + number + "EN";
    }

    public int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    public String getServerResponse(List<Message> history, int index) {
        List<Message> messages = history.subList(index, history.size());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messages", messages);
        jsonObject.put("token", getToken(history.size()));
        return jsonObject.toJSONString();
    }

    public JSONObject messageToJson(Message message){
        JSONObject jsonObject = new JSONObject();
        Long id = message.getId();
        jsonObject.put("id", id.toString());
        jsonObject.put("user", message.getUserName());
        jsonObject.put("message", message.getText());
        return jsonObject;

    }

    public String getClientSendMessageRequest(Message message) {
        JSONObject jsonObject =  messageToJson(message);
        return jsonObject.toJSONString();
    }

    public Message getClientMessage(InputStream inputStream) throws ParseException {
        JSONObject jsonObject = getJSONObject(inputStreamToString(inputStream));
        String text = (String) jsonObject.get("message");
        String user = (String) jsonObject.get("user");
        String id = (String) jsonObject.get("id");
        return new Message(Long.parseLong(id),user,text);
    }

    public Long getMessageId(InputStream inputStream) throws ParseException {
        return Long.parseLong((String) getJSONObject(inputStreamToString(inputStream)).get("id"));
    }

    public Message getChangeMessage(InputStream inputStream) throws ParseException {
        JSONObject jsonObject = getJSONObject(inputStreamToString(inputStream));
        String text = (String) jsonObject.get("message");
        String user = " ";
        String id = (String) jsonObject.get("id");
        return new Message(Long.parseLong(id),user,text);
    }

    public JSONObject getJSONObject(String json) throws ParseException {
        return (JSONObject) jsonParser.parse(json.trim());
    }

    public String inputStreamToString(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(baos.toByteArray());
    }
}