package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;

public class CloudPlayerInfoResponsePacket extends RequestPacket {
    public boolean success;
    public String name;
    public String address;
    public String currentServer;
    public String currentProxy;
    public String xuid;

    @Override
    public String encode() {
        this.addValue("success", success);
        if (success) {
            this.addValue("name", this.name);
            this.addValue("address", this.address);
            this.addValue("currentServer", this.currentServer);
            this.addValue("currentProxy", this.currentProxy);
            this.addValue("xuid", this.xuid);
        }
        return super.encode();
    }
}
