package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.tasks.KeepALiveTask;
import org.json.simple.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CloudServerConnectPacket extends DataPacket {
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        final String serverName = jsonObject.get("serverName").toString();
        final String serverPort = jsonObject.get("serverPort").toString();
        final String serverPid = jsonObject.get("serverPid").toString();

        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(serverName);
        server.setSocket(clientRequest.getSocket());
        server.setPid(Integer.parseInt(serverPid));

        Config config = new Config("./archive/server-pids/" + serverName + ".json", Config.JSON);
        config.set("pid", Integer.parseInt(serverPid));
        config.save();

        server.setAliveChecks(0);

        server.setTask(new KeepALiveTask(server));
        server.getTask().setName(server.getServerName());
        service.scheduleAtFixedRate(server.getTask(), 0, 1, TimeUnit.SECONDS);

        final VersionInfoPacket versionInfoPacket = new VersionInfoPacket();
        server.pushPacket(versionInfoPacket);
        for (final CloudServer cloudServer : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            if (cloudServer.getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
                final RegisterServerPacket packet = new RegisterServerPacket();
                packet.addValue("serverPort", serverPort);
                packet.addValue("serverName", serverName);
                cloudServer.pushPacket(packet);
            }
        }

        String notifyMessage = MessageAPI.startedMessage.replace("%service", serverName);
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        server.getTemplate().addServer(server.getTemplate(), serverName);
        server.setConnected(true);
    }
}