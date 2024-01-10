package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;

public class ServerStopEvent extends ServerEvent implements Cancellable {

    public ServerStopEvent(CloudServer server) {
        super(server);
    }
}
