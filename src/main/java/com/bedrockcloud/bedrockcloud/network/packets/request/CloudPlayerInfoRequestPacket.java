package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.CloudPlayerInfoResponsePacket;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import org.json.simple.JSONObject;

public class CloudPlayerInfoRequestPacket extends DataPacket {

    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final CloudPlayerInfoResponsePacket cloudPlayerInfoResponsePacket = new CloudPlayerInfoResponsePacket();
        cloudPlayerInfoResponsePacket.type = 1;
        cloudPlayerInfoResponsePacket.requestId = jsonObject.get("requestId").toString();

        boolean success;
        if (jsonObject.get("playerInfoName") == null) {
            success = false;
        } else {
            success = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(jsonObject.get("playerInfoName").toString()) != null;
        }

        CloudPlayer cloudPlayer;

        cloudPlayerInfoResponsePacket.success = success;
        if (success) {
            if ((cloudPlayer = BedrockCloud.getCloudPlayerProvider().getCloudPlayer(jsonObject.get("playerInfoName").toString())) != null) {
                cloudPlayerInfoResponsePacket.name = cloudPlayer.getPlayerName();
                cloudPlayerInfoResponsePacket.address = cloudPlayer.getAddress();
                cloudPlayerInfoResponsePacket.xuid = cloudPlayer.getXuid();
                cloudPlayerInfoResponsePacket.currentServer = cloudPlayer.getCurrentServer();
                cloudPlayerInfoResponsePacket.currentProxy = cloudPlayer.getCurrentProxy();
            }
        }

        BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString()).pushPacket(cloudPlayerInfoResponsePacket);
    }
}