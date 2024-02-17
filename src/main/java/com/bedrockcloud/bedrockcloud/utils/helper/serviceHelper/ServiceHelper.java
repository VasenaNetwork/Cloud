package com.bedrockcloud.bedrockcloud.utils.helper.serviceHelper;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.files.json.json;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceKiller.ServiceKiller;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ServiceHelper {

    @ApiStatus.Internal
    public static void startAllProxies() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
        for (final String name : GroupAPI.getGroups()) {
            try {
                final HashMap<String, Object> stats = (HashMap<String, Object>) json.get(name, json.ALL);
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
                final HashMap<String, Object> stats = (HashMap<String, Object>) json.get(name, json.ALL);
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
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
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
            FileManager.deleteServer(new File("./temp/" + server.getServerName()), server.getServerName(), server.getTemplate().getStatic());
        } catch (NullPointerException ex) {
            BedrockCloud.getLogger().exception(ex);
        }

        ServiceKiller.killPid(server);

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
}