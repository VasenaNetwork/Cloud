package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.ListCloudPlayersResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class ListCloudPlayersRequestPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final ListCloudPlayersResponsePacket listServerResponsePacket = new ListCloudPlayersResponsePacket();
        listServerResponsePacket.type = 1;
        listServerResponsePacket.requestId = jsonObject.get("requestId").toString();
        final CloudServer server = Cloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        server.pushPacket(listServerResponsePacket);
    }
}
