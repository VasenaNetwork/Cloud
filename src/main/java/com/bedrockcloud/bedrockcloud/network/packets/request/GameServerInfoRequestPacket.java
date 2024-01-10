package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.GameServerInfoResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class GameServerInfoRequestPacket extends DataPacket
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final GameServerInfoResponsePacket gameServerInfoResponsePacket = new GameServerInfoResponsePacket();
        gameServerInfoResponsePacket.type = 1;
        gameServerInfoResponsePacket.requestId = jsonObject.get("requestId").toString();

        CloudServer server = BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        gameServerInfoResponsePacket.serverInfoName = server.getServerName();
        gameServerInfoResponsePacket.templateName = server.getTemplate().getName();
        gameServerInfoResponsePacket.state = server.getState();
        gameServerInfoResponsePacket.isLobby = server.getTemplate().getLobby();
        gameServerInfoResponsePacket.isMaintenance = server.getTemplate().getMaintenance();
        gameServerInfoResponsePacket.isBeta = server.getTemplate().getBeta();
        gameServerInfoResponsePacket.playerCount = server.getPlayerCount();
        gameServerInfoResponsePacket.maxPlayer = server.getTemplate().getMaxPlayers();
        gameServerInfoResponsePacket.isStatic = server.getTemplate().getStatic();
        BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString()).pushPacket(gameServerInfoResponsePacket);
    }
}