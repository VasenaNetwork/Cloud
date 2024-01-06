package com.bedrockcloud.bedrockcloud.utils.helper.serviceKiller;

import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privateserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;

import java.io.File;

public class ServiceKiller {
    public static void killPid(GameServer server){
        final File pidFile = new File("./archive/server-pids/" + server.getServerName() + ".json");
        if (pidFile.exists()) {
            Config config = new Config("./archive/server-pids/" + server.getServerName() + ".json", Config.JSON);

            final ProcessBuilder builder = new ProcessBuilder();
            try {
                builder.command("/bin/sh", "-c", "kill " + config.get("pid")).start();
            } catch (Exception ignored) {}

            pidFile.delete();
        }
    }

    public static void killPid(PrivateGameServer server){
        final File pidFile = new File("./archive/server-pids/" + server.getServerName() + ".json");
        if (pidFile.exists()) {
            Config config = new Config("./archive/server-pids/" + server.getServerName() + ".json", Config.JSON);

            final ProcessBuilder builder = new ProcessBuilder();
            try {
                builder.command("/bin/sh", "-c", "kill " + config.get("pid")).start();
            } catch (Exception ignored) {}

            pidFile.delete();
        }
    }

    public static void killPid(ProxyServer server){
        final File pidFile = new File("./archive/server-pids/" + server.getServerName() + ".json");
        if (pidFile.exists()) {
            Config config = new Config("./archive/server-pids/" + server.getServerName() + ".json", Config.JSON);

            final ProcessBuilder builder = new ProcessBuilder();
            try {
                builder.command("/bin/sh", "-c", "kill " + config.get("pid")).start();
            } catch (Exception ignored) {}

            pidFile.delete();
        }
    }
}
