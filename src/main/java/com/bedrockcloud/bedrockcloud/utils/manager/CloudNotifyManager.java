package com.bedrockcloud.bedrockcloud.utils.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.CloudNotifyMessagePacket;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;
import org.jetbrains.annotations.ApiStatus;

public class CloudNotifyManager {

    @ApiStatus.Internal
    public static void sendNotifyCloud(final String message) {
        for (final ProxyServer proxy : BedrockCloud.getProxyServerProvider().getProxyServerMap().values()) {
            final CloudNotifyMessagePacket packet = new CloudNotifyMessagePacket();
            packet.message = BedrockCloud.prefix + message;
            proxy.pushPacket(packet);
        }
    }
}
