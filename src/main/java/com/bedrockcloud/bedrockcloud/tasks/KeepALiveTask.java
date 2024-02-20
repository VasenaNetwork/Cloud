package com.bedrockcloud.bedrockcloud.tasks;

import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.api.event.server.ServerTimeoutEvent;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.query.api.Protocol;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryException;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryStatus;
import com.bedrockcloud.bedrockcloud.network.packets.KeepALivePacket;
import com.bedrockcloud.bedrockcloud.BedrockCloud;

import java.io.IOException;
import java.util.ConcurrentModificationException;

/*
 * KeepALiveTask to check the status of a service
 */
public class KeepALiveTask implements Runnable {

    private final CloudServer cloudServer;

    public KeepALiveTask(CloudServer cloudServer) {
        this.cloudServer = cloudServer;
    }

    @Override
    public void run() {
        while (BedrockCloud.isRunning() && BedrockCloud.getCloudServerProvider().existServer(cloudServer.getServerName())) {
            try {
                final String serverName = cloudServer.getServerName();
                if (serverName == null) {
                    return;
                }

                final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(serverName);
                if (server == null) {
                    return;
                }

                try {
                    new QueryStatus.Builder("127.0.0.1")
                            .setProtocol(Protocol.UDP_FULL)
                            .setPort(server.getServerPort())
                            .build()
                            .getStatus()
                            .toJson();
                } catch (IllegalArgumentException ignored) {
                } catch (QueryException e) {
                    handleQueryException(server);
                }
            } catch (ConcurrentModificationException ignored) {
            }
        }
    }

    private void handleQueryException(CloudServer server) {
        if (server.getAliveChecks() == 0) {
            final KeepALivePacket packet = new KeepALivePacket();
            server.pushPacket(packet);
        } else {
            if (server.getAliveChecks() >= 10) {
                handleServerTimeout(server);
            }
        }
        if (server.getAliveChecks() < 10) {
            server.setAliveChecks(server.getAliveChecks() + 1);
        }
    }

    private void handleServerTimeout(CloudServer server) {
        server.setAliveChecks(0);

        ServerTimeoutEvent event = new ServerTimeoutEvent(server);
        BedrockCloud.getInstance().getPluginManager().callEvent(event);

        String notifyMessage = MessageAPI.timedOut.replace("%service", server.getServerName());
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        try {
            PortValidator.ports.remove(server.getServerPort());
            PortValidator.ports.remove(server.getServerPort() + 1);

            if (BedrockCloud.getTemplateProvider().isTemplateRunning(server.getTemplate())) {
                ServiceHelper.killWithPID(server);
            } else {
                ServiceHelper.killWithPID(false, server);
            }
        } catch (IOException ignored) {
        }
    }
}