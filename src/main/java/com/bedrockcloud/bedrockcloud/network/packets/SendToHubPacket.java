package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class SendToHubPacket extends DataPacket
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        for (final CloudServer cloudServer : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            if (cloudServer.getTemplate().getType() == SoftwareManager.SOFTWARE_PROXY) {
                final SendToHubPacket packet = new SendToHubPacket();
                packet.addValue("playerName", playerName);
                cloudServer.pushPacket(packet);
            }
        }
    }
}
