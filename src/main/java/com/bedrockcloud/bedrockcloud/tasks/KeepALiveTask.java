package com.bedrockcloud.bedrockcloud.tasks;

import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.query.api.Protocol;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryException;
import com.bedrockcloud.bedrockcloud.server.query.api.QueryStatus;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;

import com.bedrockcloud.bedrockcloud.network.packets.KeepALivePacket;
import com.bedrockcloud.bedrockcloud.BedrockCloud;

/*
 * KeepALiveTask to check the status of a service
 */
public class KeepALiveTask extends Thread implements Runnable {

    GameServer gameServer = null;

    public KeepALiveTask(GameServer gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void run() {
        while (BedrockCloud.isRunning() && BedrockCloud.getGameServerProvider().existServer(this.gameServer.getServerName())) {
            try {

                final String servername = this.gameServer.getServerName();
                if (servername == null) {
                    this.interrupt();
                    return;
                }
                final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(servername);
                if (gameServer == null) {
                    this.interrupt();
                    return;
                }

                try {
                    try {
                        new QueryStatus.Builder("127.0.0.1")
                                .setProtocol(Protocol.UDP_FULL)
                                .setPort(gameServer.getServerPort())
                                .build()
                                .getStatus()
                                .toJson();
                    } catch (IllegalArgumentException ignored) {
                    }
                } catch (QueryException e) {
                    if (gameServer.getAliveChecks() == 0) {
                        final KeepALivePacket packet = new KeepALivePacket();
                        gameServer.pushPacket(packet);
                    } else {
                        if (BedrockCloud.getGameServerProvider().existServer(servername)) {
                            if (gameServer.getAliveChecks() >= 10) {
                                gameServer.setAliveChecks(0);

                                String notifyMessage = MessageAPI.timedOut.replace("%service", servername);
                                CloudNotifyManager.sendNotifyCloud(notifyMessage);
                                BedrockCloud.getLogger().warning(notifyMessage);

                                try {
                                    PortValidator.ports.remove(gameServer.getServerPort());
                                    PortValidator.ports.remove(gameServer.getServerPort()+1);

                                    if (BedrockCloud.getTemplateProvider().isTemplateRunning(gameServer.getTemplate())){
                                        ServiceHelper.killWithPID(gameServer);
                                    } else {
                                        ServiceHelper.killWithPID(false, gameServer);
                                    }
                                    this.interrupt();
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                    if (gameServer.getAliveChecks() < 10) {
                        gameServer.setAliveChecks(gameServer.getAliveChecks() + 1);
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            }
        }
    }
}