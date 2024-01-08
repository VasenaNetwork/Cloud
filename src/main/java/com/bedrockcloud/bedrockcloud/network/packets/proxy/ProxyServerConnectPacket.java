package com.bedrockcloud.bedrockcloud.network.packets.proxy;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.RegisterServerPacket;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;
import org.json.simple.JSONObject;

public class ProxyServerConnectPacket extends DataPacket implements Loggable
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final String pid = jsonObject.get("proxyPid").toString();

        String notifyMessage = MessageAPI.startedMessage.replace("%service", serverName);
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        Config config = new Config("./archive/server-pids/" + serverName + ".json", Config.JSON);
        config.set("pid", Integer.parseInt(pid));
        config.save();

        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);
        proxyServer.setPid(Integer.parseInt(pid));
        final Object socketPort = jsonObject.get("socketPort");
        proxyServer.setSocket(clientRequest.getSocket());
        for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
            final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
            final RegisterServerPacket packet = new RegisterServerPacket();
            for (GameServer server : BedrockCloud.getGameServerProvider().getGameServerMap().values()) {
                if (server != null) {
                    packet.addValue("serverPort", server.getServerPort());
                    packet.addValue("serverName", server.getServerName());
                    proxy.pushPacket(packet);
                }
            }
        }
        proxyServer.setConnected(true);
    }
}
