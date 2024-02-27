package com.bedrockcloud.bedrockcloud.network.packetRegistry;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.network.packets.*;
import com.bedrockcloud.bedrockcloud.network.packets.player.CloudPlayerChangeServerPacket;
import com.bedrockcloud.bedrockcloud.network.packets.player.CloudPlayerJoinPacket;
import com.bedrockcloud.bedrockcloud.network.packets.player.CloudPlayerQuitPacket;
import com.bedrockcloud.bedrockcloud.network.packets.request.*;
import com.bedrockcloud.bedrockcloud.network.packets.response.*;

public class PacketRegistry {

    public static void registerPackets() {
        Cloud.getPacketHandler().registerPacket(CloudServerConnectPacket.class);
        Cloud.getPacketHandler().registerPacket(CloudServerDisconnectPacket.class);
        Cloud.getPacketHandler().registerPacket(CloudServerInfoRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(CloudServerInfoResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(ListServerRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(ListServerResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(ListCloudPlayersRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(ListCloudPlayersResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(PlayerMessagePacket.class);
        Cloud.getPacketHandler().registerPacket(CloudPlayerJoinPacket.class);
        Cloud.getPacketHandler().registerPacket(CloudPlayerQuitPacket.class);
        Cloud.getPacketHandler().registerPacket(UnregisterServerPacket.class);
        Cloud.getPacketHandler().registerPacket(RegisterServerPacket.class);
        Cloud.getPacketHandler().registerPacket(KeepALivePacket.class);
        Cloud.getPacketHandler().registerPacket(UpdateGameServerInfoPacket.class);
        Cloud.getPacketHandler().registerPacket(VersionInfoPacket.class);
        Cloud.getPacketHandler().registerPacket(CloudNotifyMessagePacket.class);
        Cloud.getPacketHandler().registerPacket(PlayerMovePacket.class);
        Cloud.getPacketHandler().registerPacket(PlayerKickPacket.class);
        Cloud.getPacketHandler().registerPacket(CloudPlayerChangeServerPacket.class);
        Cloud.getPacketHandler().registerPacket(SendToHubPacket.class);
        Cloud.getPacketHandler().registerPacket(ListTemplatesRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(ListTemplatesResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(TemplateInfoRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(TemplateInfoResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(CloudPlayerInfoRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(CloudPlayerInfoResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(CheckPlayerMaintenanceRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(CheckPlayerMaintenanceResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(ServerStartRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(ServerStopRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(ServerStartResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(ServerStopResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(StartTemplateRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(StopTemplateRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(StartTemplateResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(StopTemplateResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(SaveServerRequestPacket.class);
        Cloud.getPacketHandler().registerPacket(SaveServerResponsePacket.class);
        Cloud.getPacketHandler().registerPacket(TemplateUpdatePacket.class);
    }
}
