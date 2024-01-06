package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.ConcurrentModificationException;

public class ListServerResponsePacket extends RequestPacket
{
    @Override
    public String encode() {
        final JSONArray arr = new JSONArray();
        try {
            for (final GameServer key : BedrockCloud.getGameServerProvider().getGameServerMap().values()) {
                if (key.getSocket() != null && key.getTemplate().runningTemplateServers.get(key.getServerName()) != null) {
                    arr.add(key.getServerName());
                }
            }
        } catch (ConcurrentModificationException e){
            BedrockCloud.getLogger().exception(e);
        }
        this.addValue("servers", JSONValue.toJSONString(arr));
        return super.encode();
    }
}
