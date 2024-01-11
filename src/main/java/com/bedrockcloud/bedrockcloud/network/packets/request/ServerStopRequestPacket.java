package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.ServerStopResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.json.simple.JSONObject;

public class ServerStopRequestPacket extends DataPacket {
    private static int FAILURE_SERVER_EXISTENCE = 0;

    @Override
    public void handle(JSONObject jsonObject, ClientRequest clientRequest) {
        final ServerStopResponsePacket serverStopResponsePacket = new ServerStopResponsePacket();

        final String server_Name = jsonObject.get("serverName").toString();
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(server_Name);
        if (server == null) {
            serverStopResponsePacket.success = false;
            serverStopResponsePacket.failureId = FAILURE_SERVER_EXISTENCE;
            BedrockCloud.getLogger().error("This Server doesn't exist");
        } else {
            serverStopResponsePacket.success = true;
            serverStopResponsePacket.serverInfoName = server_Name;
            server.stopServer();
        }

        serverStopResponsePacket.requestId = jsonObject.get("requestId").toString();
        final CloudServer cloudServer = BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        cloudServer.pushPacket(serverStopResponsePacket);
    }
}
