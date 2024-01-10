package com.bedrockcloud.bedrockcloud.network.packetRegistry;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.*;
import com.bedrockcloud.bedrockcloud.network.packets.cloudplayer.CloudPlayerChangeServerPacket;
import com.bedrockcloud.bedrockcloud.network.packets.proxy.ProxyPlayerJoinPacket;
import com.bedrockcloud.bedrockcloud.network.packets.proxy.ProxyPlayerQuitPacket;
import com.bedrockcloud.bedrockcloud.network.packets.proxy.ProxyServerConnectPacket;
import com.bedrockcloud.bedrockcloud.network.packets.proxy.ProxyServerDisconnectPacket;
import com.bedrockcloud.bedrockcloud.network.packets.request.*;
import com.bedrockcloud.bedrockcloud.network.packets.response.*;

public class PacketRegistry {

    public static void registerPackets() {
        BedrockCloud.getPacketHandler().registerPacket(GameServerConnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(GameServerDisconnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ProxyServerConnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ProxyServerDisconnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudServerInfoRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudServerInfoResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListServerRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListServerResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListCloudPlayersRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListCloudPlayersResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(PlayerMessagePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ProxyPlayerJoinPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ProxyPlayerQuitPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(UnregisterServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(RegisterServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(KeepALivePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(UpdateGameServerInfoPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StartGroupPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StartServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StopGroupPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StopServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(VersionInfoPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudNotifyMessagePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(PlayerMovePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(PlayerKickPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudPlayerChangeServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(SendToHubPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListTemplatesRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListTemplatesResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(TemplateInfoRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(TemplateInfoResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudPlayerInfoRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudPlayerInfoResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CheckPlayerMaintenanceRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CheckPlayerMaintenanceResponsePacket.class);
    }
}
