package com.bedrockcloud.bedrockcloud.rest.handler.server;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceKiller.ServiceKiller;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerStopRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQueryParams(query);

        if (queryParams.size() == 0){
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "No arguments given!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        if (queryParams.get("server") == null){
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Missing argument: 'server'");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        String server = queryParams.get("server");

        if (BedrockCloud.getCloudServerProvider().existServer(server)) {
            final CloudServer cloudServer = BedrockCloud.getCloudServerProvider().getServer(server);
            ServiceKiller.killPid(cloudServer);

            JSONObject responseObj = new JSONObject();
            responseObj.put("success", "The server " + server + " was stopped!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "The server " + server + " don't exists!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? pair.substring(0, idx) : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }
}
