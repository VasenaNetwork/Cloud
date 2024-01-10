package com.bedrockcloud.bedrockcloud.rest.handler.server;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class ServerListRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (BedrockCloud.getCloudServerProvider().getCloudServers().size() == 0) {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "No servers online!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        JSONArray serversArray = new JSONArray();
        JSONObject serversObj = new JSONObject();
        for (String server : BedrockCloud.getCloudServerProvider().getCloudServers().keySet()) {
            if (BedrockCloud.getCloudServerProvider().existServer(server)) {
                serversObj.put("name", server);
                serversArray.add(serversObj);
            }
        }

        JSONObject responseObj = new JSONObject();
        responseObj.put("servers", serversArray);

        String response = responseObj.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
