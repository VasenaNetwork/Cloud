package com.bedrockcloud.bedrockcloud.network.packets.cloudplayer;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class CloudPlayerChangeServerPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String toServer = jsonObject.get("serverName").toString();
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(toServer);
            BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).setCurrentServer(toServer);
            server.getTemplate().removePlayer(BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName));
            server.getTemplate().addPlayer(BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName), toServer);
        }
        super.handle(jsonObject, clientRequest);
    }
}
