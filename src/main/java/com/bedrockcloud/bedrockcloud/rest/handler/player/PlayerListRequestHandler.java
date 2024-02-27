package com.bedrockcloud.bedrockcloud.rest.handler.player;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class PlayerListRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (Cloud.getCloudPlayerProvider().getCloudPlayerMap().size() == 0){
            String response = "No players online!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        JSONArray playersArray = new JSONArray();
        for (String cloudPlayerName : Cloud.getCloudPlayerProvider().getCloudPlayerMap().keySet()){
            if (Cloud.getCloudPlayerProvider().existsPlayer(cloudPlayerName)) {
                JSONObject playerObj = new JSONObject();
                playerObj.put("name", cloudPlayerName);
                playerObj.put("currentServer", Cloud.getCloudPlayerProvider().getCloudPlayer(cloudPlayerName).getCurrentServer());
                playersArray.add(playerObj);
            }
        }

        JSONObject responseObj = new JSONObject();
        responseObj.put("players", playersArray);

        String response = responseObj.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
