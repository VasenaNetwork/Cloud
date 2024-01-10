package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;

public class ServerTimeoutEvent extends ServerEvent {
    public ServerTimeoutEvent(CloudServer server) {
        super(server);
    }
}
