package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.StopTemplateResponsePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONObject;

public class StopTemplateRequestPacket extends DataPacket {
    private static int FAILURE_TEMPLATE_EXISTENCE = 0;
    private static int FAILURE_TEMPLATE_NOT_RUNNING = 1;

    @Override
    public void handle(JSONObject jsonObject, ClientRequest clientRequest) {
        final StopTemplateResponsePacket pk = new StopTemplateResponsePacket();
        pk.type = 1;

        final String templateName = jsonObject.get("templateName").toString();
        final Template template = BedrockCloud.getTemplateProvider().getTemplate(templateName);
        if (template == null) {
            pk.success = false;
            pk.failureId = FAILURE_TEMPLATE_EXISTENCE;
            BedrockCloud.getLogger().error("This group is not exist");
        }
        else if (!BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
            pk.success = false;
            pk.failureId = FAILURE_TEMPLATE_NOT_RUNNING;
            BedrockCloud.getLogger().error("The group is not running!");
        } else {
            pk.success = true;
            pk.templateName = templateName;

            template.stop();
        }

        pk.requestId = jsonObject.get("requestId").toString();
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString());
        server.pushPacket(pk);
    }
}
