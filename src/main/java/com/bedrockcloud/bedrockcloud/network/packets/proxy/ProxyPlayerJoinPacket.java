package com.bedrockcloud.bedrockcloud.network.packets.proxy;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class ProxyPlayerJoinPacket extends DataPacket
{
    private String playerName;
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playername = jsonObject.get("playerName").toString();
        final String serverName = jsonObject.get("joinedServer").toString();
        BedrockCloud.getCloudPlayerProvider().addCloudPlayer(new CloudPlayer(jsonObject.get("playerName").toString().toLowerCase(), jsonObject.get("address").toString(), jsonObject.get("uuid").toString(), jsonObject.get("xuid").toString(), jsonObject.get("currentServer").toString(), jsonObject.get("currentProxy").toString()));
        if (BedrockCloud.getCloudServerProvider().existServer(serverName)) {
            final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(serverName);
            final ProxyPlayerJoinPacket packet = new ProxyPlayerJoinPacket();

            server.getTemplate().addPlayer(BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName), serverName);

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
