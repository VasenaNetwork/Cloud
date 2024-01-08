package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.server.privateserver.PrivateGameServer;

public class PrivateServerStopEvent extends Event implements Cancellable {

    private final PrivateGameServer server;

    public PrivateServerStopEvent(PrivateGameServer server) {
        this.server = server;
    }

    public PrivateGameServer getServer() {
        return server;
    }
}