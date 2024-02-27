package com.bedrockcloud.bedrockcloud.network.packets.player;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.event.player.CloudPlayerJoinEvent;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class CloudPlayerJoinPacket extends DataPacket
{
    private String playerName;
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playername = jsonObject.get("playerName").toString();
        final String serverName = jsonObject.get("joinedServer").toString();

        Cloud.getCloudPlayerProvider().addCloudPlayer(new CloudPlayer(jsonObject.get("playerName").toString().toLowerCase(), jsonObject.get("address").toString(), jsonObject.get("uuid").toString(), jsonObject.get("xuid").toString(), jsonObject.get("currentServer").toString(), jsonObject.get("currentProxy").toString()));
        CloudPlayerJoinEvent event = new CloudPlayerJoinEvent(Cloud.getCloudPlayerProvider().getCloudPlayer(jsonObject.get("playerName").toString().toLowerCase()));
        Cloud.getInstance().getPluginManager().callEvent(event);

        if (Cloud.getCloudServerProvider().existServer(serverName)) {
            final CloudServer server = Cloud.getCloudServerProvider().getServer(serverName);
            final CloudPlayerJoinPacket packet = new CloudPlayerJoinPacket();

            server.getTemplate().addPlayer(Cloud.getCloudPlayerProvider().getCloudPlayer(playerName), serverName);

            packet.playerName = playername;
            server.pushPacket(packet);
        }
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        return super.encode();
    }
}
