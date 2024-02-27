package com.bedrockcloud.bedrockcloud.network.packets.player;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.event.player.CloudPlayerSwitchServerEvent;
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
        if (Cloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudServer server = Cloud.getCloudServerProvider().getServer(toServer);

            CloudPlayerSwitchServerEvent event = new CloudPlayerSwitchServerEvent(Cloud.getCloudPlayerProvider().getCloudPlayer(playerName), Cloud.getCloudPlayerProvider().getCloudPlayer(playerName).getCurrentServer(), toServer);
            Cloud.getInstance().getPluginManager().callEvent(event);

            Cloud.getCloudPlayerProvider().getCloudPlayer(playerName).setCurrentServer(toServer);
            server.getTemplate().removePlayer(Cloud.getCloudPlayerProvider().getCloudPlayer(playerName));
            server.getTemplate().addPlayer(Cloud.getCloudPlayerProvider().getCloudPlayer(playerName), toServer);
        }
        super.handle(jsonObject, clientRequest);
    }
}
