package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;

public class ServerStartEvent extends ServerEvent {
    public ServerStartEvent(CloudServer server) {
        super(server);
    }
}