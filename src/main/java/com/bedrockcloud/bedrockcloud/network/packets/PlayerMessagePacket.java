package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import org.json.simple.JSONObject;

public class PlayerMessagePacket extends DataPacket
{
    public String playerName;
    public String value;
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String value = jsonObject.get("value").toString();
        if (Cloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudPlayer cloudPlayer = Cloud.getCloudPlayerProvider().getCloudPlayer(playerName);
            final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
            playerTextPacket.playerName = cloudPlayer.getPlayerName();
            playerTextPacket.type = 0;
            playerTextPacket.value = value;
            cloudPlayer.getProxy().pushPacket(playerTextPacket);
        }
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("type", 0);
        this.addValue("value", this.value);
        return super.encode();
    }
}
