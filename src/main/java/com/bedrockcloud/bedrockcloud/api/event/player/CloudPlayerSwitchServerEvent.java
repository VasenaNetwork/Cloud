package com.bedrockcloud.bedrockcloud.api.event.player;

import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import lombok.Getter;

public class CloudPlayerSwitchServerEvent extends PlayerEvent {

    @Getter
    private String oldServer;
    @Getter
    private String newServer;

    /**
     * Creates a new {@link PlayerEvent}
     *
     * @param player who represents the player which comes with this event
     */
    public CloudPlayerSwitchServerEvent(CloudPlayer player, String oldServer, String newServer) {
        super(player);
        this.oldServer = oldServer;
        this.newServer = newServer;
    }
}
