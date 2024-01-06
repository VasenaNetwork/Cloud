package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.TemplateInfoResponsePacket;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONObject;

public class TemplateInfoRequestPacket extends DataPacket {

    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final TemplateInfoResponsePacket templateInfoResponsePacket = new TemplateInfoResponsePacket();
        templateInfoResponsePacket.type = 1;
        templateInfoResponsePacket.requestId = jsonObject.get("requestId").toString();
        Template template;
        if (jsonObject.get("template") == null) {
            template = BedrockCloud.getPrivategameServerProvider().getGameServer(jsonObject.get("serverName").toString()).getTemplate();
        } else {
            template = BedrockCloud.getTemplateProvider().getTemplate(jsonObject.get("templateName").toString());
        }
        templateInfoResponsePacket.templateName = template.getName();
        templateInfoResponsePacket.isLobby = template.getLobby();
        templateInfoResponsePacket.isMaintenance = template.getMaintenance();
        templateInfoResponsePacket.isPrivate = template.getCanBePrivate();
        templateInfoResponsePacket.isBeta = template.getBeta();
        templateInfoResponsePacket.maxPlayer = template.getMaxPlayers();
        BedrockCloud.getPrivategameServerProvider().getGameServer(jsonObject.get("serverName").toString()).pushPacket(templateInfoResponsePacket);
    }
}