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

import java.io.IOException;
import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.network.packets.KeepALivePacket;
import com.bedrockcloud.bedrockcloud.BedrockCloud;

/*
 * KeepALiveTask to check the status of a service
 */
public class KeepALiveTask extends Thread implements Runnable {

    CloudServer cloudServer = null;

    public KeepALiveTask(CloudServer cloudServer){
        this.cloudServer = cloudServer;
    }

    @Override
    public void run() {
        while (BedrockCloud.isRunning() && BedrockCloud.getCloudServerProvider().existServer(this.cloudServer.getServerName())) {
            try {

                final String servername = this.cloudServer.getServerName();
                if (servername == null) {
                    this.interrupt();
                    return;
                }

                final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(servername);
                if (server == null) {
                    this.interrupt();
                    return;
                }

                try {
                    try {
                        new QueryStatus.Builder("127.0.0.1")
                                .setProtocol(Protocol.UDP_FULL)
                                .setPort(server.getServerPort())
                                .build()
                                .getStatus()
                                .toJson();
                    } catch (IllegalArgumentException ignored) {
                    }
                } catch (QueryException e) {
                    if (server.getAliveChecks() == 0) {
                        final KeepALivePacket packet = new KeepALivePacket();
                        server.pushPacket(packet);
                    } else {
                        if (BedrockCloud.getCloudServerProvider().existServer(servername)) {
                            if (server.getAliveChecks() >= 10) {
                                server.setAliveChecks(0);

                                ServerTimeoutEvent event = new ServerTimeoutEvent(server);
                                BedrockCloud.getInstance().getPluginManager().callEvent(event);

                                String notifyMessage = MessageAPI.timedOut.replace("%service", servername);
                                CloudNotifyManager.sendNotifyCloud(notifyMessage);
                                BedrockCloud.getLogger().warning(notifyMessage);

                                try {
                                    PortValidator.ports.remove(server.getServerPort());
                                    PortValidator.ports.remove(server.getServerPort()+1);

                                    if (BedrockCloud.getTemplateProvider().isTemplateRunning(server.getTemplate())){
                                        ServiceHelper.killWithPID(server);
                                    } else {
                                        ServiceHelper.killWithPID(false, server);
                                    }
                                    this.interrupt();
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                    if (server.getAliveChecks() < 10) {
                        server.setAliveChecks(server.getAliveChecks() + 1);
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            }
        }
    }
}