package com.bedrockcloud.bedrockcloud.rest.handler.plugin;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.plugin.Plugin;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class PluginListRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (Cloud.getCloudServerProvider().getCloudServers().size() == 0) {
            JSONObject responseObj = new JSONObject();
            responseObj.put("error", "No plugins!");

            String response = responseObj.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        JSONArray pluginsArray = new JSONArray();
        JSONObject pluginsObj = new JSONObject();
        for (Plugin plugin : Cloud.getInstance().getPluginManager().getPlugins()) {
            if (plugin.isEnabled()) {
                pluginsObj.put("name", plugin.getName());
                pluginsArray.add(pluginsObj);
            }
        }

        JSONObject responseObj = new JSONObject();
        responseObj.put("plugins", pluginsArray);

        String response = responseObj.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}