package com.bedrockcloud.bedrockcloud.server.properties;

import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privateserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public class ServerProperties {

    @ApiStatus.Internal
    public static void createProperties(final GameServer gameServer) {
        final String serverName = gameServer.getServerName();
        final int port = gameServer.getServerPort();
        final StringBuilder sb = new StringBuilder();
        Objects.requireNonNull(gameServer);
        final String filePath = sb.append("./temp/").append(serverName).append("/server.properties").toString();
        Config prop = new Config(filePath, Config.PROPERTIES);
        try {
            prop.set("server-port", Integer.toString(port));
            prop.set("language", "deu");
            prop.set("motd", serverName);
            prop.set("white-list", "false");
            prop.set("announce-player-achievements", "off");
            prop.set("spawn-protection", "0");
            prop.set("max-players", Integer.toString(gameServer.getTemplate().getMaxPlayers()));
            prop.set("online-players", "0");
            prop.set("gamemode", "0");
            prop.set("force-gamemode", "off");
            prop.set("hardcore", "off");
            prop.set("pvp", "on");
            prop.set("difficulty", "1");
            prop.set("enable-query", "on");
            prop.set("enable-rcon", "off");
            prop.set("rcon.password", "gayorso");
            prop.set("auto-save", "off");
            prop.set("view-distance", "8");
            prop.set("xbox-auth", "off");
            prop.set("enable-ipv6", "off");
            prop.set("template", gameServer.getTemplate().getName());
            prop.set("cloud-port", String.valueOf(Utils.getConfig().getDouble("port")));
            prop.set("cloud-password", Utils.getConfig().getString("password"));
            prop.set("cloud-path", Utils.getCloudPath());
            prop.set("is-private", false);
            prop.save();
        } catch (Throwable ignored) {}
    }

    @ApiStatus.Internal
    public static void createProperties(final PrivateGameServer gameServer) {
        final String serverName = gameServer.getServerName();
        final int port = gameServer.getServerPort();
        final StringBuilder sb = new StringBuilder();
        Objects.requireNonNull(gameServer);
        final String filePath = sb.append("./temp/").append(serverName).append("/server.properties").toString();
        Config prop = new Config(filePath, Config.PROPERTIES);
        try {
            prop.set("server-port", Integer.toString(port));
            prop.set("language", "deu");
            prop.set("motd", serverName);
            prop.set("white-list", "false");
            prop.set("announce-player-achievements", "off");
            prop.set("spawn-protection", "0");
            prop.set("max-players", Integer.toString(gameServer.getTemplate().getMaxPlayers()));
            prop.set("online-players", "0");
            prop.set("gamemode", "0");
            prop.set("force-gamemode", "off");
            prop.set("hardcore", "off");
            prop.set("pvp", "on");
            prop.set("difficulty", "1");
            prop.set("enable-query", "on");
            prop.set("enable-rcon", "off");
            prop.set("rcon.password", "gayorso");
            prop.set("auto-save", "off");
            prop.set("view-distance", "8");
            prop.set("xbox-auth", "off");
            prop.set("enable-ipv6", "off");
            prop.set("template", gameServer.getTemplate().getName());
            prop.set("cloud-port", String.valueOf(Utils.getConfig().getDouble("port")));
            prop.set("cloud-password", Utils.getConfig().getString("password"));
            prop.set("cloud-path", Utils.getCloudPath());
            prop.set("is-private", true);
            prop.set("pserver-owner", gameServer.getServerOwner());
            prop.save();
        } catch (Throwable ignored) {}
    }

    @ApiStatus.Internal
    public static void createProperties(final ProxyServer proxyServer) {
        final Config proxy = new Config("./temp/" + proxyServer.getServerName() + "/config.yml", Config.YAML);
        proxy.set("listener.host", "0.0.0.0:" + proxyServer.getServerPort());
        proxy.set("listener.max_players", proxyServer.getTemplate().getMaxPlayers());
        if (proxyServer.getTemplate().getMaintenance()) {
            proxy.set("listener.motd", "§c§oMaintenance");
        } else {
            proxy.set("listener.motd", Utils.getConfig().getString("motd"));
        }
        proxy.set("use_login_extras", Utils.getConfig().get("wdpe-login-extras"));
        proxy.set("inject_proxy_commands", false);
        proxy.set("replace_username_spaces", true);
        proxy.set("cloud-path", Utils.getCloudPath());
        proxy.set("cloud-port", String.valueOf(Utils.getConfig().getDouble("port")));
        proxy.save();
    }
}