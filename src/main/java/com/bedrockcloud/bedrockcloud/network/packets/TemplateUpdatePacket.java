package com.bedrockcloud.bedrockcloud.network.packets;

import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.templates.Template;

public class TemplateUpdatePacket extends DataPacket {
    private final Template template;

    public TemplateUpdatePacket(Template template) {
        this.template = template;
    }

    @Override
    public String encode() {
        this.addValue("templateName", this.template.getName());
        this.addValue("isLobby", this.template.getLobby());
        this.addValue("isMaintenance", this.template.getMaintenance());
        this.addValue("isBeta", this.template.getBeta());
        this.addValue("maxPlayer", this.template.getMaxPlayers());
        this.addValue("isStatic", this.template.getStatic());
        this.addValue("type", this.template.getType());
        return super.encode();
    }
}
