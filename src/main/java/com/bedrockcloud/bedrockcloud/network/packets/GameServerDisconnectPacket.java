package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import com.bedrockcloud.bedrockcloud.server.privateserver.PrivateGameServer;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONObject;

import java.io.File;

public class GameServerDisconnectPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final boolean isPrivate = Boolean.parseBoolean(jsonObject.get("isPrivate").toString());

        if (!isPrivate) {
            final GameServer gameServer = BedrockCloud.getGameServerProvider().getGameServer(serverName);
            gameServer.setAliveChecks(0);
            final Template template = gameServer.getTemplate();
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final UnregisterServerPacket packet = new UnregisterServerPacket();
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            PortValidator.ports.remove(gameServer.getServerPort());
            PortValidator.ports.remove(gameServer.getServerPort()+1);

            try {
                FileManager.deleteServer(new File("./temp/" + serverName), serverName, gameServer.getTemplate().getStatic());
            } catch (NullPointerException ex) {
                BedrockCloud.getLogger().exception(ex);
            }

            String notifyMessage = MessageAPI.stoppedMessage.replace("%service", gameServer.getServerName());
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            gameServer.getTemplate().removeServer(gameServer.getServerName());
            BedrockCloud.getGameServerProvider().removeServer(gameServer.getServerName());

            if (template.getRunningTemplateServers().size() < template.getMinRunningServer()) {
                if (BedrockCloud.getTemplateProvider().isTemplateRunning(template) && !template.getMaintenance()) {
                    new GameServer(template);
                }
            }
        } else {
            final PrivateGameServer gameServer = BedrockCloud.getPrivategameServerProvider().getGameServer(serverName);
            gameServer.setAliveChecks(0);

            final Template template = gameServer.getTemplate();
            for (final String key : BedrockCloud.getProxyServerProvider().getProxyServerMap().keySet()) {
                final ProxyServer proxy = BedrockCloud.getProxyServerProvider().getProxyServer(key);
                final UnregisterServerPacket packet = new UnregisterServerPacket();
                packet.addValue("serverName", serverName);
                proxy.pushPacket(packet);
            }

            PortValidator.ports.remove(gameServer.getServerPort());
            PortValidator.ports.remove((gameServer.getServerPort()+1));

            try {
                FileManager.deleteServer(new File("./temp/" + serverName), serverName, false);
            } catch (NullPointerException ex) {
                BedrockCloud.getLogger().exception(ex);
            }

            String notifyMessage = MessageAPI.stoppedMessage.replace("%service", gameServer.getServerName());
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            gameServer.getTemplate().removeServer(gameServer.getServerName());
            BedrockCloud.getPrivategameServerProvider().removeServer(gameServer.getServerName());
        }
    }
}
