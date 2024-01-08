package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.GameServerInfoResponsePacket;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privateserver.PrivateGameServer;
import org.json.simple.JSONObject;

public class GameServerInfoRequestPacket extends DataPacket
{
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final boolean isPrivate = Boolean.parseBoolean(jsonObject.get("isPrivate").toString());

        final GameServerInfoResponsePacket gameServerInfoResponsePacket = new GameServerInfoResponsePacket();
        gameServerInfoResponsePacket.type = 1;
        gameServerInfoResponsePacket.requestId = jsonObject.get("requestId").toString();

        if (!isPrivate) {
            GameServer server;
            if (jsonObject.get("serverInfoName") == null) {
                server = BedrockCloud.getGameServerProvider().getGameServer(jsonObject.get("serverName").toString());
            } else {
                server = BedrockCloud.getGameServerProvider().getGameServer(jsonObject.get("serverInfoName").toString());
            }
            gameServerInfoResponsePacket.serverInfoName = server.getServerName();
            gameServerInfoResponsePacket.templateName = server.getTemplate().getName();
            gameServerInfoResponsePacket.state = server.getState();
            gameServerInfoResponsePacket.isLobby = server.getTemplate().getLobby();
            gameServerInfoResponsePacket.isMaintenance = server.getTemplate().getMaintenance();
            gameServerInfoResponsePacket.isPrivate = false;
            gameServerInfoResponsePacket.isBeta = server.getTemplate().getBeta();
            gameServerInfoResponsePacket.playerCount = server.getPlayerCount();
            gameServerInfoResponsePacket.maxPlayer = server.getTemplate().getMaxPlayers();
            gameServerInfoResponsePacket.isStatic = server.getTemplate().getStatic();
            BedrockCloud.getGameServerProvider().getGameServer(jsonObject.get("serverName").toString()).pushPacket(gameServerInfoResponsePacket);
        } else {
            PrivateGameServer server;
            if (jsonObject.get("serverInfoName") == null) {
                server = BedrockCloud.getPrivategameServerProvider().getGameServer(jsonObject.get("serverName").toString());
            } else {
                server = BedrockCloud.getPrivategameServerProvider().getGameServer(jsonObject.get("serverInfoName").toString());
            }
            gameServerInfoResponsePacket.serverInfoName = server.getServerName();
            gameServerInfoResponsePacket.templateName = server.getTemplate().getName();
            gameServerInfoResponsePacket.state = server.getState();
            gameServerInfoResponsePacket.isLobby = server.getTemplate().getLobby();
            gameServerInfoResponsePacket.isMaintenance = server.getTemplate().getMaintenance();
            gameServerInfoResponsePacket.isPrivate = false;
            gameServerInfoResponsePacket.isBeta = server.getTemplate().getBeta();
            gameServerInfoResponsePacket.isStatic = server.getTemplate().getStatic();
            gameServerInfoResponsePacket.playerCount = server.getPlayerCount();
            gameServerInfoResponsePacket.maxPlayer = server.getTemplate().getMaxPlayers();
            gameServerInfoResponsePacket.isStatic = server.getTemplate().getStatic();
            BedrockCloud.getPrivategameServerProvider().getGameServer(jsonObject.get("serverName").toString()).pushPacket(gameServerInfoResponsePacket);
        }
    }
}