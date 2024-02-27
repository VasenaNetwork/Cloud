package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.ConcurrentModificationException;

public class ListTemplatesResponsePacket extends RequestPacket
{

    @Override
    public String encode() {
        final JSONArray arr = new JSONArray();
        try {
            for (final Template key : Cloud.getTemplateProvider().getRunningTemplates().values()) {
                if (key.getName() != null) {
                    if (key.getType() == SoftwareManager.SOFTWARE_SERVER) {
                        arr.add(key.getName());
                    }
                }
            }
        } catch (ConcurrentModificationException e){
            Cloud.getLogger().exception(e);
        }
        this.addValue("templates", JSONValue.toJSONString(arr));
        return super.encode();
    }
}
