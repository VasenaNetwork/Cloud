package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;

public class CheckPlayerMaintenanceResponsePacket extends RequestPacket {
    public boolean success;
    public String name;

    @Override
    public String encode() {
        this.addValue("success", success);
        this.addValue("name", name);
        return super.encode();
    }
}
