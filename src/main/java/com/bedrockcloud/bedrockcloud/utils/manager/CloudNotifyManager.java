package com.bedrockcloud.bedrockcloud.utils.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.network.packets.CloudNotifyMessagePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.jetbrains.annotations.ApiStatus;

public class CloudNotifyManager {

    @ApiStatus.Internal
    public static void sendNotifyCloud(final String message) {
        for (final CloudServer cloudServer : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            if (cloudServer.getTemplate().getType() == SoftwareManager.SOFTWARE_PROXY) {
                final CloudNotifyMessagePacket packet = new CloudNotifyMessagePacket();
                packet.message = BedrockCloud.prefix + message;
                cloudServer.pushPacket(packet);
            }
        }
    }
}
