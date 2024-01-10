package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class PlayerMessagePacket extends DataPacket
{
    public String playerName;
    public String value;
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String value = jsonObject.get("value").toString();
        if (BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            final CloudPlayer cloudPlayer = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName);
            final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
            playerTextPacket.playerName = cloudPlayer.getPlayerName();
            playerTextPacket.type = 0;
            playerTextPacket.value = value;
            cloudPlayer.getProxy().pushPacket(playerTextPacket);
        } else if (playerName.equals("all.players")){
            final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
            playerTextPacket.playerName = playerName;
            playerTextPacket.type = 0;
            playerTextPacket.value = value;
            for (final CloudServer server : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
                if (server.getTemplate().getType() == SoftwareManager.SOFTWARE_PROXY) {
                    server.pushPacket(playerTextPacket);
                }
            }
        } else if (playerName.equals("team.members")){
            final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
            playerTextPacket.playerName = playerName;
            playerTextPacket.type = 0;
            playerTextPacket.value = value;
            for (final CloudServer server : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
                if (server.getTemplate().getType() == SoftwareManager.SOFTWARE_PROXY) {
                    server.pushPacket(playerTextPacket);
                }
            }
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
