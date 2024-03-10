package com.bedrockcloud.bedrockcloud.tasks;

import com.bedrockcloud.bedrockcloud.utils.Messages;
import com.bedrockcloud.bedrockcloud.api.event.server.ServerTimeoutEvent;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.ServerUtils;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.bedrockcloud.bedrockcloud.utils.PortValidator;
import com.bedrockcloud.bedrockcloud.server.query.api.Protocol;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryException;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryStatus;
import com.bedrockcloud.bedrockcloud.network.packets.KeepALivePacket;
import com.bedrockcloud.bedrockcloud.Cloud;

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
        while (Cloud.isRunning() && Cloud.getCloudServerProvider().existServer(cloudServer.getServerName())) {
            try {
                final String serverName = cloudServer.getServerName();
                if (serverName == null) {
                    return;
                }

                final CloudServer server = Cloud.getCloudServerProvider().getServer(serverName);
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
        Cloud.getInstance().getPluginManager().callEvent(event);

        String notifyMessage = Messages.timedOut.replace("%service", server.getServerName());
        Utils.sendNotifyCloud(notifyMessage);
        Cloud.getLogger().warning(notifyMessage);

        try {
            PortValidator.getUsedPorts().remove(server.getServerPort());
            PortValidator.getUsedPorts().remove(server.getServerPort() + 1);

            if (Cloud.getTemplateProvider().isTemplateRunning(server.getTemplate())) {
                ServerUtils.killWithPID(server);
            } else {
                ServerUtils.killWithPID(false, server);
            }
        } catch (IOException ignored) {
        }
    }
}