package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONObject;

import java.io.File;

public class CloudServerDisconnectPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();

        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(serverName);
        server.setAliveChecks(0);
        final Template template = server.getTemplate();
        for (final CloudServer cloudServer : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            if (cloudServer.getTemplate().getType() == SoftwareManager.SOFTWARE_PROXY) {
                final UnregisterServerPacket packet = new UnregisterServerPacket();
                packet.addValue("serverName", serverName);
                cloudServer.pushPacket(packet);
            }
        }

        PortValidator.ports.remove(server.getServerPort());
        PortValidator.ports.remove(server.getServerPort() + 1);

        try {
            FileManager.deleteServer(new File("./temp/" + serverName), serverName, server.getTemplate().getStatic());
        } catch (NullPointerException ex) {
            BedrockCloud.getLogger().exception(ex);
        }

        String notifyMessage = MessageAPI.stoppedMessage.replace("%service", server.getServerName());
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        server.getTemplate().removeServer(server.getServerName());
        BedrockCloud.getCloudServerProvider().removeServer(server.getServerName());

        if (template.getRunningTemplateServers().size() < template.getMinRunningServer()) {
            if (BedrockCloud.getTemplateProvider().isTemplateRunning(template) && !template.getMaintenance()) {
                new CloudServer(template);
            }
        }
    }
}
