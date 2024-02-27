package com.bedrockcloud.bedrockcloud.network.packets.response;

import com.bedrockcloud.bedrockcloud.network.packets.RequestPacket;

public class TemplateInfoResponsePacket extends RequestPacket
{
    public String templateName;
    public boolean isLobby;
    public boolean isMaintenance;
    public boolean isBeta;
    public int maxPlayer;
    public boolean isStatic;
    public int templateType;

    @Override
    public String encode() {
        this.addValue("templateName", this.templateName);
        this.addValue("isLobby", this.isLobby);
        this.addValue("isMaintenance", this.isMaintenance);
        this.addValue("isBeta", this.isBeta);
        this.addValue("maxPlayer", this.maxPlayer);
        this.addValue("isStatic", this.isStatic);
        this.addValue("type", this.templateType);
        return super.encode();
    }
}