package com.bedrockcloud.bedrockcloud.player;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import org.jetbrains.annotations.ApiStatus;

public class CloudPlayer
{
    private final String playerName;
    private final String address;
    private final String uuid;
    private String currentServer;
    private final String xuid;
    private final String currentProxy;
    
    public CloudPlayer(String playerName, String address, String uuid, String xuid, String currentServer, String currentProxy) {
        this.playerName = playerName.replace(" ", "_");
        this.address = address;
        this.uuid = uuid;
        this.xuid = xuid;
        this.currentServer = currentServer;
        this.currentProxy = currentProxy;
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public String getCurrentProxy() {
        return this.currentProxy;
    }
    
    public String getCurrentServer() {
        return this.currentServer;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public String getXuid() {
        return this.xuid;
    }

    @ApiStatus.Internal
    public void setCurrentServer(final String serverName) {
        this.currentServer = serverName;
    }
    
    public CloudServer getProxy() {
        return BedrockCloud.getCloudServerProvider().getServer(this.getCurrentProxy());
    }

    @Override
    public String toString() {
        return "CloudPlayer{playerName='" + this.playerName + '\'' + ", address='" + this.address + '\'' + ", uuid='" + this.uuid + '\'' + ", currentServer='" + this.currentServer + '\'' + ", xuid='" + this.xuid + '\'' + ", currentProxy='" + this.currentProxy + '\'' + '}';
    }
}
