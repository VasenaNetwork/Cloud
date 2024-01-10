package com.bedrockcloud.bedrockcloud.network.packets.proxy;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.RegisterServerPacket;
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

        final CloudServer cloudServer = BedrockCloud.getCloudServerProvider().getServer(serverName);
        cloudServer.setPid(Integer.parseInt(pid));

        cloudServer.setSocket(clientRequest.getSocket());
        for (final CloudServer server : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            final RegisterServerPacket packet = new RegisterServerPacket();
            for (CloudServer proxy : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
                if (server != null && proxy.getTemplate().getType() == SoftwareManager.SOFTWARE_PROXY && server.getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
                    packet.addValue("serverPort", server.getServerPort());
                    packet.addValue("serverName", server.getServerName());
                    proxy.pushPacket(packet);
                }
            }
        }
        cloudServer.setConnected(true);
    }
}
