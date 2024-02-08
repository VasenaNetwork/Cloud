package com.bedrockcloud.bedrockcloud.network.packetRegistry;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.*;
import com.bedrockcloud.bedrockcloud.network.packets.player.CloudPlayerChangeServerPacket;
import com.bedrockcloud.bedrockcloud.network.packets.player.CloudPlayerJoinPacket;
import com.bedrockcloud.bedrockcloud.network.packets.player.CloudPlayerQuitPacket;
import com.bedrockcloud.bedrockcloud.network.packets.request.*;
import com.bedrockcloud.bedrockcloud.network.packets.response.*;

public class PacketRegistry {

    public static void registerPackets() {
        BedrockCloud.getPacketHandler().registerPacket(CloudServerConnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudServerDisconnectPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudServerInfoRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudServerInfoResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListServerRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListServerResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListCloudPlayersRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ListCloudPlayersResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(PlayerMessagePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudPlayerJoinPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(CloudPlayerQuitPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(UnregisterServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(RegisterServerPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(KeepALivePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(UpdateGameServerInfoPacket.class);
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
        BedrockCloud.getPacketHandler().registerPacket(ServerStartRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ServerStopRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ServerStartResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(ServerStopResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StartTemplateRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StopTemplateRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StartTemplateResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(StopTemplateResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(SaveServerRequestPacket.class);
        BedrockCloud.getPacketHandler().registerPacket(SaveServerResponsePacket.class);
        BedrockCloud.getPacketHandler().registerPacket(TemplateUpdatePacket.class);
    }
}
