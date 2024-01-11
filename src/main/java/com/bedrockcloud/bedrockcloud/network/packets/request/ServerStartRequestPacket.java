package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.ListServerResponsePacket;
import com.bedrockcloud.bedrockcloud.network.packets.response.ServerStartResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ServerStartRequestPacket extends DataPacket {
    private static int FAILURE_TEMPLATE_EXISTENCE = 0;
    private static int FAILURE_GROUP_RUNNING = 1;

    @Override
    public void handle(JSONObject jsonObject, ClientRequest clientRequest) {
        final ServerStartResponsePacket serverStartResponsePacket = new ServerStartResponsePacket();

        final String groupName = jsonObject.get("groupName").toString();
        final String count = jsonObject.get("count").toString();
        final Template group = BedrockCloud.getTemplateProvider().getTemplate(groupName);
        if (group == null) {
            serverStartResponsePacket.success = false;
            serverStartResponsePacket.failureId = FAILURE_TEMPLATE_EXISTENCE;
            BedrockCloud.getLogger().error("This group does not exist");
        }
        else if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
            serverStartResponsePacket.success = false;
            serverStartResponsePacket.failureId = FAILURE_GROUP_RUNNING;
            BedrockCloud.getLogger().error("The group is not running");
        } else {
            final JSONArray arr = new JSONArray();
            for (int i = 0; i < Integer.parseInt(count); ++i) {
                CloudServer server = new CloudServer(group);
                arr.add(server.getServerName());
            }
            serverStartResponsePacket.success = true;
            serverStartResponsePacket.startedServers = arr;
        }

        serverStartResponsePacket.requestId = jsonObject.get("requestId").toString();
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        server.pushPacket(serverStartResponsePacket);
    }
}
