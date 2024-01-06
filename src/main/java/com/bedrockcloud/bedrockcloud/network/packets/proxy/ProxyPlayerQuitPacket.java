package com.bedrockcloud.bedrockcloud.network.packets.proxy;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privateserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;
import org.json.simple.JSONObject;

public class ProxyPlayerQuitPacket extends DataPacket
{
    private String playerName;

    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playername = jsonObject.get("playerName").toString();
        final String serverName = jsonObject.get("leftServer").toString();

        if (BedrockCloud.getGameServerProvider().existServer(serverName)) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            final ProxyPlayerQuitPacket packet = new ProxyPlayerQuitPacket();
            packet.playerName = playername;
            gameServer.pushPacket(packet);
        } else if (BedrockCloud.getPrivategameServerProvider().existServer(serverName)){
            final PrivateGameServer gameServer = BedrockCloud.getPrivategameServerProvider().getGameServer(serverName);
            final ProxyPlayerQuitPacket packet = new ProxyPlayerQuitPacket();
            packet.playerName = playername;
            gameServer.pushPacket(packet);
        }

        final CloudPlayer cloudPlayer = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playername);
        if (cloudPlayer.isHasPrivateServer()){
            for (PrivateGameServer server : BedrockCloud.getPrivategameServerProvider().getGameServerMap().values()) {
                if (server.getServerOwner().equals(cloudPlayer.getPlayerName())){
                    server.stopServer();
                }
            }
        }

        final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
        final PrivateGameServer privateGameServer = BedrockCloud.getPrivategameServerProvider().getGameServer(serverName);
        final ProxyServer proxyServer = BedrockCloud.getProxyServerProvider().getProxyServer(serverName);

        if (gameServer != null) gameServer.getTemplate().removePlayer(cloudPlayer);
        if (privateGameServer != null) privateGameServer.getTemplate().removePlayer(cloudPlayer);
        if (proxyServer != null) proxyServer.getTemplate().removePlayer(cloudPlayer);

        BedrockCloud.getCloudPlayerProvider().removeCloudPlayer(cloudPlayer);
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        return super.encode();
    }
}
