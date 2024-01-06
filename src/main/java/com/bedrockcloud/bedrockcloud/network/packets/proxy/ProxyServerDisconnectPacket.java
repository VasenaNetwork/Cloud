package com.bedrockcloud.bedrockcloud.network.packets.proxy;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;
import org.json.simple.JSONObject;

import java.io.File;

public class ProxyServerDisconnectPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);
        if (proxyServer != null) {
            try {
                String notifyMessage = MessageAPI.stoppedMessage.replace("%service", proxyServer.getServerName());
                CloudNotifyManager.sendNotifyCloud(notifyMessage);
                BedrockCloud.getLogger().warning(notifyMessage);

                FileManager.deleteServer(new File("./temp/" + serverName), serverName, proxyServer.getTemplate().getStatic());
            } catch (NullPointerException ex) {
                BedrockCloud.getLogger().exception(ex);
            }

            PortValidator.ports.remove(proxyServer.getServerPort());
            PortValidator.ports.remove((proxyServer.getServerPort()+1));

            for (CloudPlayer player : BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().values()) {
                if (player != null) {
                    if (player.getProxy().getServerName().equals(proxyServer.getServerName())) {
                        BedrockCloud.getCloudPlayerProvider().removeCloudPlayer(player);
                    }
                }
            }

            proxyServer.getTemplate().removeServer(proxyServer.getServerName());
            BedrockCloud.getProxyServerProvider().removeServer(proxyServer.getServerName());
        }
    }
}
