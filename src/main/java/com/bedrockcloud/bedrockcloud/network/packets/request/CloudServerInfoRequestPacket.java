package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.CloudServerInfoResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class CloudServerInfoRequestPacket extends DataPacket
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final CloudServerInfoResponsePacket cloudServerInfoResponsePacket = new CloudServerInfoResponsePacket();
        cloudServerInfoResponsePacket.type = 1;
        cloudServerInfoResponsePacket.requestId = jsonObject.get("requestId").toString();

        CloudServer server = BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        cloudServerInfoResponsePacket.serverInfoName = server.getServerName();
        cloudServerInfoResponsePacket.templateName = server.getTemplate().getName();
        cloudServerInfoResponsePacket.state = server.getState();
        cloudServerInfoResponsePacket.isLobby = server.getTemplate().isLobby();
        cloudServerInfoResponsePacket.isMaintenance = server.getTemplate().isMaintenance();
        cloudServerInfoResponsePacket.isBeta = server.getTemplate().isBeta();
        cloudServerInfoResponsePacket.playerCount = server.getPlayerCount();
        cloudServerInfoResponsePacket.maxPlayer = server.getTemplate().getMaxPlayers();
        cloudServerInfoResponsePacket.isStatic = server.getTemplate().isStatic();
        BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString()).pushPacket(cloudServerInfoResponsePacket);
    }
}