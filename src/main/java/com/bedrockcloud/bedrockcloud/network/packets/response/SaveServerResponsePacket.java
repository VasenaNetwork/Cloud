package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;

public class SaveServerResponsePacket extends RequestPacket {
    public boolean success;
    public int failureId;

    @Override
    public String encode() {
        this.addValue("success", success);
        if (!this.success) {
            this.addValue("failureId", failureId);
        }
        return super.encode();
    }
}
