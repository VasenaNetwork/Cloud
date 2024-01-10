package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;

public class ServerStartEvent extends ServerEvent implements Cancellable {
    public ServerStartEvent(CloudServer server) {
        super(server);
    }
}