package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;

public class StopTemplateResponsePacket extends RequestPacket {
    public boolean success;
    public int failureId;
    public String templateName;

    @Override
    public String encode() {
        this.addValue("success", success);
        if (!success) {
            this.addValue("failureId", failureId);
        } else {
            this.addValue("templateName", templateName);
        }
        return super.encode();
    }
}
