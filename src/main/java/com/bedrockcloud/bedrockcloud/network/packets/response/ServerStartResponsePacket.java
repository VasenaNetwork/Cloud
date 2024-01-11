package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class ServerStartResponsePacket extends RequestPacket {
    public boolean success;
    public int failureId;
    public JSONArray startedServers;
    @Override
    public String encode() {
        this.addValue("success", success);
        if (!this.success) {
            this.addValue("failureId", failureId);
        } else {
            this.addValue("servers", JSONValue.toJSONString(startedServers));
        }
        return super.encode();
    }
}
