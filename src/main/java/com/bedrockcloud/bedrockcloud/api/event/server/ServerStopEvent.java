package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;

public class ServerStopEvent extends Event implements Cancellable {

    private final GameServer server;

    public ServerStopEvent(GameServer server) {
        this.server = server;
    }

    public GameServer getServer() {
        return server;
    }
}
