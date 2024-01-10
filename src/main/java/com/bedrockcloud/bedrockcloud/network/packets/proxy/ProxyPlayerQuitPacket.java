package com.bedrockcloud.bedrockcloud.network.packets.proxy;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.event.player.CloudPlayerQuitEvent;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class ProxyPlayerQuitPacket extends DataPacket
{
    private String playerName;

    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playername = jsonObject.get("playerName").toString();
        final String serverName = jsonObject.get("leftServer").toString();

        if (BedrockCloud.getCloudServerProvider().existServer(serverName)) {
            final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(serverName);
            final ProxyPlayerQuitPacket packet = new ProxyPlayerQuitPacket();
            packet.playerName = playername;
            server.pushPacket(packet);
        }

        final CloudPlayer cloudPlayer = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playername);
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(serverName);

        if (server != null) server.getTemplate().removePlayer(cloudPlayer);
        String proxy = cloudPlayer.getCurrentProxy();
        if (BedrockCloud.getCloudServerProvider().getServer(proxy) != null) BedrockCloud.getCloudServerProvider().getServer(proxy).getTemplate().removePlayer(cloudPlayer);
        CloudPlayerQuitEvent event = new CloudPlayerQuitEvent(cloudPlayer);
        BedrockCloud.getInstance().getPluginManager().callEvent(event);

        BedrockCloud.getCloudPlayerProvider().removeCloudPlayer(cloudPlayer);
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        return super.encode();
    }
}
