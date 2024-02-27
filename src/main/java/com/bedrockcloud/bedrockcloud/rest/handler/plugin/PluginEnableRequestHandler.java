package com.bedrockcloud.bedrockcloud.rest.handler.plugin;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.plugin.Plugin;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class PluginEnableRequestHandler implements HttpHandler {
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

        if (queryParams.get("plugin") == null){
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Missing argument: 'plugin'");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        String plugin = queryParams.get("plugin");
        if (Cloud.getInstance().getPluginManager().getPluginByName(plugin) != null) {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "Plugin " + plugin + " already exists");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            Plugin loadedPlugin = Cloud.getInstance().getPluginManager().loadPlugin(new File("./local/plugins/cloud/" + plugin + ".jar").toPath());
            if (loadedPlugin != null) {
                if (Cloud.getInstance().getPluginManager().enablePlugin(loadedPlugin, null)) {
                    JSONObject responseObj = new JSONObject();
                    responseObj.put("success", "Plugin " + plugin + " enabled");

                    String response = responseObj.toString();
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    JSONObject responseObj = new JSONObject();
                    responseObj.put("error", "Plugin " + plugin + " can't be enabled");

                    String response = responseObj.toString();
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } else {
                JSONObject responseObj = new JSONObject();
                responseObj.put("error", "Plugin " + plugin + " can't be enabled");

                String response = responseObj.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
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
