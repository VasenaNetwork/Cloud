package com.bedrockcloud.bedrockcloud.server.cloudserver;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public class CloudServerProvider {

    @ApiStatus.Internal
    private final Map<String, CloudServer> cloudServerMap;

    public CloudServerProvider() {
        this.cloudServerMap = new HashMap<>();
    }

    public Map<String, CloudServer> getCloudServers() {
        return this.cloudServerMap;
    }

    @ApiStatus.Internal
    public void addServer(final CloudServer cloudServer) {
        this.cloudServerMap.put(cloudServer.getServerName(), cloudServer);
    }

    @ApiStatus.Internal
    public void removeServer(final CloudServer cloudServer) {
        this.cloudServerMap.remove(cloudServer.getServerName());
    }

    @ApiStatus.Internal
    public void removeServer(final String name) {
        this.cloudServerMap.remove(name);
    }

    public CloudServer getServer(final String name) {
        return this.cloudServerMap.get(name);
    }

    public boolean existServer(final String name) {
        return this.cloudServerMap.get(name) != null;
    }
}
