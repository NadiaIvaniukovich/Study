package serverclient;

/**
 * Created by Hope on 3/14/2015.
 */
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements HttpHandler {

    private static final Logger log = Logger.getLogger(Server.class);


    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Usage: java Server port");
        else {
            try {
                System.out.println("Server is starting...");
                Integer port = Integer.parseInt(args[0]);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Get list of messages: GET http://" + serverHost + ":" + port + "/chat?token={token}");
                System.out.println("Send message: POST http://" + serverHost + ":" + port + "/chat provide body json in format {\"message\" : \"{message}\"} ");

                server.createContext("/chat", new Server());
                server.setExecutor(null);
                server.start();
                log.debug("server started\n");
            } catch (IOException e) {
                System.out.println("Error creating http server: " + e);
            }
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";

        try {
            log.debug("request begin");
            if ("GET".equals(httpExchange.getRequestMethod())) {
                response = doGet(httpExchange);
            } else if ("POST".equals(httpExchange.getRequestMethod())) {
                doPost(httpExchange);
            } else if ("DELETE".equals(httpExchange.getRequestMethod())) {
                doDelete(httpExchange);
            } else if ("PUT".equals(httpExchange.getRequestMethod())) {
                doPut(httpExchange);
            } else if ("OPTIONS".equals(httpExchange.getRequestMethod())) {
                response = "";
            } else {
                response = "Unsupported http method: " + httpExchange.getRequestMethod();
            }

            log.debug("response: history size=" + history.size());
            sendResponse(httpExchange, response);
        } catch (Exception e) {
            log.debug("response: error=" + e.getMessage(), e);
            response = e.getMessage();
            sendResponse(httpExchange, response, 500);
        }

        log.debug("request end\n");
    }

    private String doGet(HttpExchange httpExchange) {
        log.debug("method GET");
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            if (token != null && !"".equals(token)) {
                int index = messageExchange.getIndex(token);
                log.debug("request parameters: token=" + token);
                return messageExchange.getServerResponse(history,index);
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        return  "Absent query in url";
    }

    private void doPost(HttpExchange httpExchange) {
        try {
            log.debug("method POST");
            Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            System.out.println("Get Message from "+message.getUserName()+" : " + message.getText()+"(id:"+message.getId()+")");
            log.debug("get message");
            history.add(message);
            log.debug("add message to history");
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doDelete(HttpExchange httpExchange) {
        try {
            Long messageId = messageExchange.getMessageId(httpExchange.getRequestBody());
            for (int i=0; i<history.size(); i++) {
                if(history.get(i).getId().equals(messageId)){
                    history.remove(i);
                    System.out.println("Message " + messageId + " deleted.");
                    return;
                }
            }
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doPut(HttpExchange httpExchange) {
        try {
            Message message = messageExchange.getChangeMessage(httpExchange.getRequestBody());
            for (int i=0; i<history.size(); i++) {
                if(history.get(i).getId().equals(message.getId())){
                    history.get(i).setText(message.getText());
                    System.out.println("Message " + message.getId() + " changed to " + message.getText());
                    return;
                }
            }
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void sendResponse(HttpExchange httpExchange, String response) {
        sendResponse(httpExchange, response, 200);
    }

    private void sendResponse(HttpExchange httpExchange, String response, int code) {
        try {
            byte[] bytes = response.getBytes();
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin","*");
            if("OPTIONS".equals(httpExchange.getRequestMethod())) {
                headers.add("Access-Control-Allow-Methods","PUT, DELETE, POST, GET, OPTIONS");
            }
            httpExchange.sendResponseHeaders(code, bytes.length);
            OutputStream os = httpExchange.getResponseBody();
            os.write( bytes);
            os.flush();
            log.debug("response sent");
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
