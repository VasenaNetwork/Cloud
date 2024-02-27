package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.ServerStartResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ServerStartRequestPacket extends DataPacket {
    private static int FAILURE_TEMPLATE_EXISTENCE = 0;
    private static int FAILURE_TEMPLATE_RUNNING = 1;

    @Override
    public void handle(JSONObject jsonObject, ClientRequest clientRequest) {
        final ServerStartResponsePacket serverStartResponsePacket = new ServerStartResponsePacket();
        serverStartResponsePacket.type = 1;

        final String templateName = jsonObject.get("templateName").toString();
        final String count = jsonObject.get("count").toString();
        final Template group = Cloud.getTemplateProvider().getTemplate(templateName);
        if (group == null) {
            serverStartResponsePacket.success = false;
            serverStartResponsePacket.failureId = FAILURE_TEMPLATE_EXISTENCE;
            Cloud.getLogger().error("This template does not exist");
        } else if (!Cloud.getTemplateProvider().isTemplateRunning(group)) {
            serverStartResponsePacket.success = false;
            serverStartResponsePacket.failureId = FAILURE_TEMPLATE_RUNNING;
            Cloud.getLogger().error("The template is not running");
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
        final CloudServer server = Cloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        server.pushPacket(serverStartResponsePacket);
    }
}
