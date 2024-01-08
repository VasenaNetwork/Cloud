package com.bedrockcloud.bedrockcloud.api.event.player;

import com.bedrockcloud.bedrockcloud.player.CloudPlayer;

public class CloudPlayerQuitEvent extends PlayerEvent {
    /**
     * Creates a new {@link PlayerEvent}
     *
     * @param player who represents the player which comes with this event
     */
    public CloudPlayerQuitEvent(CloudPlayer player) {
        super(player);
    }
}
