package com.bedrockcloud.bedrockcloud.utils;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.files.json.JsonUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ServerUtils {
    @ApiStatus.Internal
    public static void startAllProxies() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
        for (final String name : GroupAPI.getGroups()) {
            try {
                final HashMap<String, Object> stats = (HashMap<String, Object>) JsonUtils.get(name, JsonUtils.ALL);
                if (stats != null && !stats.isEmpty()) {
                    if (Integer.parseInt(stats.get("type").toString()) == 0) {
                        final Template group = BedrockCloud.getTemplateProvider().getTemplate(name);
                        if (group != null) {
                            if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
                                group.start(false);
                            }
                        }
                    }
                }
            } catch (IOException e2) {
                BedrockCloud.getLogger().exception(e2);
            }
        }
    }

    @ApiStatus.Internal
    public static void startAllServers() {
        for (final String name : GroupAPI.getGroups()) {
            try {
                final HashMap<String, Object> stats = (HashMap<String, Object>) JsonUtils.get(name, JsonUtils.ALL);
                if (stats != null && !stats.isEmpty()) {
                    if (Integer.parseInt(stats.get("type").toString()) == 1) {
                        final Template group = BedrockCloud.getTemplateProvider().getTemplate(name);
                        if (group != null) {
                            if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
                                group.start(false);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }

    @ApiStatus.Internal
    public static void killWithPID(CloudServer server) throws IOException {
        killWithPID(true, server);
    }

    @ApiStatus.Internal
    public static void killWithPID(boolean startNewService, CloudServer server) throws IOException {
        String notifyMessage = MessageAPI.stoppedMessage.replace("%service", server.getServerName());
        Utils.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        Template template = server.getTemplate();

        final ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.command("/bin/sh", "-c", "screen -X -S " + server.getServerName() + " kill").start();
        } catch (Exception ignored) {
        }
        try {
            builder.command("/bin/sh", "-c", "kill " + server.getPid()).start();
        } catch (Exception ignored) {
        }

        try {
            FileUtils.deleteServer(new File("./temp/" + server.getServerName()), server.getServerName(), server.getTemplate().isStatic());
        } catch (NullPointerException ex) {
            BedrockCloud.getLogger().exception(ex);
        }

        killPid(server);

        server.getTemplate().removeServer(server.getServerName());
        BedrockCloud.getCloudServerProvider().removeServer(server.getServerName());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        if (!startNewService) return;
        if (BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
            if (server.getTemplate().getRunningServers().size() < server.getTemplate().getMinRunningServer()) {
                new CloudServer(template);
            }
        }
    }

    @ApiStatus.Internal
    public static void killPid(CloudServer server){
        final File file = new File("./archive/processes/" + server.getServerName() + ".json");
        if (file.exists()) {
            Config config = new Config("./archive/processes/" + server.getServerName() + ".json", Config.JSON);

            final ProcessBuilder builder = new ProcessBuilder();
            try {
                builder.command("/bin/sh", "-c", "kill " + config.get("pid")).start();
            } catch (Exception ignored) {}

            file.delete();
        }
    }
}
