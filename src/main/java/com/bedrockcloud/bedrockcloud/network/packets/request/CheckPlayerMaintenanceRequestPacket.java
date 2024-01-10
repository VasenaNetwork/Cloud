package com.bedrockcloud.bedrockcloud.network.packets.request;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import com.bedrockcloud.bedrockcloud.network.packets.response.CheckPlayerMaintenanceResponsePacket;
import com.bedrockcloud.bedrockcloud.utils.manager.MaintenanceManager;
import org.json.simple.JSONObject;

public class CheckPlayerMaintenanceRequestPacket extends DataPacket {

    @Override
    public void handle(JSONObject jsonObject, ClientRequest clientRequest) {
        final CheckPlayerMaintenanceResponsePacket checkPlayerMaintenanceResponsePacket = new CheckPlayerMaintenanceResponsePacket();
        checkPlayerMaintenanceResponsePacket.type = 1;
        checkPlayerMaintenanceResponsePacket.requestId = jsonObject.get("requestId").toString();

        boolean success;
        if (jsonObject.get("playerInfoName") == null) {
            success = false;
        } else {
            success = MaintenanceManager.isMaintenance(jsonObject.get("playerInfoName").toString());
        }

        checkPlayerMaintenanceResponsePacket.success = success;
        checkPlayerMaintenanceResponsePacket.name = jsonObject.get("playerInfoName").toString();

        BedrockCloud.getCloudServerProvider().getServer(jsonObject.get("serverName").toString()).pushPacket(checkPlayerMaintenanceResponsePacket);
    }
}
