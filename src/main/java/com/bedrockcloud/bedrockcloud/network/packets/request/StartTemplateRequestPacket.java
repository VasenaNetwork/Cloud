package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.StartTemplateResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StartTemplateRequestPacket extends DataPacket {
    private static int FAILURE_TEMPLATE_EXISTENCE = 0;
    private static int FAILURE_TEMPLATE_RUNNING = 1;

    @Override
    public void handle(JSONObject jsonObject, ClientRequest clientRequest) {
        final StartTemplateResponsePacket pk = new StartTemplateResponsePacket();

        final String templateName = jsonObject.get("templateName").toString();
        final Template template = BedrockCloud.getTemplateProvider().getTemplate(templateName);
        if (template == null) {
            pk.success = false;
            pk.failureId = FAILURE_TEMPLATE_EXISTENCE;
            this.getLogger().error("This template does not exist");
        }
        else if (BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
            pk.success = false;
            pk.failureId = FAILURE_TEMPLATE_RUNNING;
            this.getLogger().error("The template is already running!");
        } else {
            final JSONArray arr = new JSONArray();
            arr.add(templateName);

            pk.success = true;
            pk.startedTemplate = arr;

            template.start(true);
        }

        pk.requestId = jsonObject.get("requestId").toString();
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        server.pushPacket(pk);
    }
}
