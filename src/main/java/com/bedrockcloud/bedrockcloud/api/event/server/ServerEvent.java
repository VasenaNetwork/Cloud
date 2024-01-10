package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;

public class ServerEvent extends Event {

    private final CloudServer server;

    public ServerEvent(CloudServer server) {
        this.server = server;
    }

    public CloudServer getServer() {
        return server;
    }
}
