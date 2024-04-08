package com.bedrockcloud.bedrockcloud.network;

import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;

public class DataPacket implements Loggable
{
    public final int TYPE_REQUEST = 0;
    public final int TYPE_RESPONSE = 1;
    private String serverName;
    public Map<String, Object> data;

    public DataPacket() {
        this.data = new HashMap<>();
    }

    public String getServerName() {
        return this.serverName;
    }

    public void addValue(final String key, final String value) {
        this.data.put(key, value);
    }

    public void addValue(final String key, final int value) {
        this.data.put(key, value);
    }

    public void addValue(final String key, final boolean value) {
        this.data.put(key, value);
    }

    public void addValue(final String key, final Map value) {
        this.data.put(key, value);
    }

    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        this.serverName = jsonObject.get("serverName").toString();
    }

    public String encode() {
        this.addValue("packetName", this.getClass().getSimpleName());
        return JSONValue.toJSONString(this.data);
    }
}
