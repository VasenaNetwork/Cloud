package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.server.gameserver.GameServer;

public class ServerStartEvent extends Event {
    private GameServer server;

    public ServerStartEvent(GameServer server) {
        this.server = server;
    }

    public GameServer getServer() {
        return server;
    }
}