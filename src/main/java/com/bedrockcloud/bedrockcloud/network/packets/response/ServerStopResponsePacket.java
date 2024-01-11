package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;

public class ServerStopResponsePacket extends RequestPacket {
    public boolean success;
    public int failureId;
    public String serverName;

    @Override
    public String encode() {
        this.addValue("success", success);
        if (!success) {
            this.addValue("failureId", failureId);
        } else {
            this.addValue("serverName", serverName);
        }
        return super.encode();
    }
}
