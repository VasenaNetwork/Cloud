package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;
import com.bedrockcloud.bedrockcloud.templates.Template;
import org.json.simple.JSONArray;

import java.util.ConcurrentModificationException;
import java.util.Objects;

public class TemplateInfoResponsePacket extends RequestPacket
{
    public String templateName;
    public boolean isLobby;
    public boolean isPrivate;
    public boolean isMaintenance;
    public boolean isBeta;
    public int maxPlayer;

    @Override
    public String encode() {
        this.addValue("templateName", this.templateName);
        this.addValue("isLobby", this.isLobby);
        this.addValue("isPrivate", this.isPrivate);
        this.addValue("isMaintenance", this.isMaintenance);
        this.addValue("isBeta", this.isBeta);
        this.addValue("maxPlayer", this.maxPlayer);
        return super.encode();
    }
}