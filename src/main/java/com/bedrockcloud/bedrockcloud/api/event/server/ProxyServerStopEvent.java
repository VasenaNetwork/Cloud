package com.bedrockcloud.bedrockcloud.api.event.server;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.server.proxyserver.ProxyServer;

public class ProxyServerStopEvent extends Event implements Cancellable {

    private final ProxyServer server;

    public ProxyServerStopEvent(ProxyServer server) {
        this.server = server;
    }

    public ProxyServer getServer() {
        return server;
    }
}