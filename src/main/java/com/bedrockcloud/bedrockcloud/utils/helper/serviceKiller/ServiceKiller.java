package com.bedrockcloud.bedrockcloud.utils.helper.serviceKiller;

import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;

public class ServiceKiller {

    @ApiStatus.Internal
    public static void killPid(CloudServer server){
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
