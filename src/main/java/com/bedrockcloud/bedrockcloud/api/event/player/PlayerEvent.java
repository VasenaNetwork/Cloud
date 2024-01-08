package com.bedrockcloud.bedrockcloud.api.event.player;

import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;

public abstract class PlayerEvent extends Event {

    private final CloudPlayer player;

    /**
     * Creates a new {@link PlayerEvent}
     *
     * @param player who represents the player which comes with this event
     */
    public PlayerEvent( CloudPlayer player ) {
        this.player = player;
    }

    /**
     * Retrives the {@link CloudPlayer} which comes with this {@link PlayerEvent}
     *
     * @return a fresh {@link CloudPlayer}
     */
    public CloudPlayer getPlayer() {
        return this.player;
    }
}