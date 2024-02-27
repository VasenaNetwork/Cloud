package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class PlayerKickPacket extends DataPacket
{
    public String playerName;
    public String reason;
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String reason = jsonObject.get("reason").toString();
        if (Cloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudPlayer cloudPlayer = Cloud.getCloudPlayerProvider().getCloudPlayer(playerName);
            if (Cloud.getCloudServerProvider().existServer(cloudPlayer.getCurrentProxy())) {
                final CloudServer cloudServer = Cloud.getCloudServerProvider().getServer(cloudPlayer.getCurrentProxy());
                final PlayerKickPacket playerKickPacket = new PlayerKickPacket();
                playerKickPacket.playerName = playerName;
                playerKickPacket.reason = reason.replace("§", "&");
                cloudServer.pushPacket(playerKickPacket);
            }
        } else {
            //this.getLogger().error(playerName + " is not connected with the BedrockCloud!");
        }
        super.handle(jsonObject, clientRequest);
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("reason", this.reason);
        return super.encode();
    }
}
