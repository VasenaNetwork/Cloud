package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class VerificationPacket extends DataPacket
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(serverName);
        server.setSocket(clientRequest.getSocket());
    }
}
