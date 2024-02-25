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
        this.addValue("isLobby", this.template.isLobby());
        this.addValue("isMaintenance", this.template.isMaintenance());
        this.addValue("isBeta", this.template.isBeta());
        this.addValue("maxPlayer", this.template.getMaxPlayers());
        this.addValue("isStatic", this.template.isStatic());
        this.addValue("type", this.template.getType());
        return super.encode();
    }
}
