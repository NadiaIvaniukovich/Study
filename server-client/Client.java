package serverclient;

/**
 * Created by Hope on 3/14/2015.
 */
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {

    private static final Logger log = Logger.getLogger(Client.class);

    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();
    private String host;
    private Integer port;
    private String username = "User"+(int)(Math.random() * 501);

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
    }


    public static void main(String[] args) {
        if (args.length != 2)
            System.out.println("Usage: java ChatClient host port");
        else {
            System.out.println("Connection to server...");
            String serverHost = args[0];
            Integer serverPort = Integer.parseInt(args[1]);
            Client client = new Client(serverHost, serverPort);
            new Thread(client).start();
            log.debug("client started\n");
            System.out.println("Connected to server: " + serverHost + ":" + serverPort);
            client.listen();
        }
    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/chat?token=" + messageExchange.getToken(history.size()));
        log.debug("request parameters: token="+messageExchange.getToken(history.size()));
        log.debug("request: history size="+history.size());
        return (HttpURLConnection) url.openConnection();
    }

    public List<Message> getMessages() {
        List<Message> list = new ArrayList<Message>();
        HttpURLConnection connection = null;
        try {
            log.debug("GET request");
            connection = getHttpURLConnection();
            connection.connect();
            String response = messageExchange.inputStreamToString(connection.getInputStream());
            JSONObject jsonObject = messageExchange.getJSONObject(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("messages");
            for (Object o : jsonArray) {
                JSONObject j = ((JSONObject) o);
                Message message = new Message(Integer.parseInt((String)j.get("id")),(String)j.get("user"),(String)j.get("message"));
                System.out.println(message.getUserName()+":"+message.getText());
                list.add(message);
            }
            log.debug("get response\n");
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return list;
    }

    public void sendMessage(String text) {
        HttpURLConnection connection = null;
        try {

            log.debug("POST request");
            connection = getHttpURLConnection();
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            Message message = new Message(username, text);

            byte[] bytes = messageExchange.getClientSendMessageRequest(message).getBytes();
            wr.write(bytes, 0, bytes.length);
            wr.flush();
            wr.close();

            connection.getInputStream();
            log.debug("sent message");

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void listen() {
        while (true) {
            List<Message> list = getMessages();

            if (list.size() > 0) {
                history.addAll(list);
                log.debug("get message\n");
            }

            log.debug("end request\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            sendMessage(message);
            log.debug("end request\n");
        }
    }
}
