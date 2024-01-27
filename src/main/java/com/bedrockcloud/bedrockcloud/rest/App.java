package com.bedrockcloud.bedrockcloud.rest;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.rest.handler.player.PlayerInfoRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.player.PlayerKickRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.player.PlayerListRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.server.ServerListRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.server.ServerStartRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.server.ServerStopRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.template.TemplateDeleteRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.template.TemplateRestartRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.template.TemplateStartRequestHandler;
import com.bedrockcloud.bedrockcloud.rest.handler.template.TemplateStopRequestHandler;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public App(){
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress((int) Utils.getConfig().getDouble("rest-port")), 0);

            Authenticator authenticator = new BasicAuthenticator("cloud") {
                @Override
                public boolean checkCredentials(String username, String password) {
                    return username.equals(Utils.getConfig().getString("rest-username")) && password.equals(Utils.getConfig().getString("rest-password"));
                }
            };

            //Template
            server.createContext("/api/v1/template/start/", new TemplateStartRequestHandler()).setAuthenticator(authenticator);
            server.createContext("/api/v1/template/stop/", new TemplateStopRequestHandler()).setAuthenticator(authenticator);
            server.createContext("/api/v1/template/restart/", new TemplateRestartRequestHandler()).setAuthenticator(authenticator);
            server.createContext("/api/v1/template/delete/", new TemplateDeleteRequestHandler()).setAuthenticator(authenticator);

            //Player
            server.createContext("/api/v1/player/kick/", new PlayerKickRequestHandler()).setAuthenticator(authenticator);
            server.createContext("/api/v1/player/list/", new PlayerListRequestHandler()).setAuthenticator(authenticator);
            server.createContext("/api/v1/player/info/", new PlayerInfoRequestHandler()).setAuthenticator(authenticator);

            //Server
            server.createContext("/api/v1/server/list/", new ServerListRequestHandler()).setAuthenticator(authenticator);
            server.createContext("/api/v1/server/start/", new ServerStartRequestHandler()).setAuthenticator(authenticator);
            server.createContext("/api/v1/server/stop/", new ServerStopRequestHandler()).setAuthenticator(authenticator);

            server.setExecutor(null);
            server.start();
            BedrockCloud.getLogger().info("§aRestAPI is running.");
        } catch (IOException e) {
            BedrockCloud.getLogger().error("§cCan't create RestAPI server.");
        }
    }
}
