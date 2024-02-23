package com.bedrockcloud.bedrockcloud.server.cloudserver;

import java.util.HashMap;
import java.util.Map;

public class CloudServerProvider {

    private final Map<String, CloudServer> cloudServerMap;

    public CloudServerProvider() {
        this.cloudServerMap = new HashMap<>();
    }

    public Map<String, CloudServer> getCloudServers() {
        return this.cloudServerMap;
    }

    public void addServer(CloudServer cloudServer) {
        this.cloudServerMap.put(cloudServer.getServerName(), cloudServer);
    }

    public void removeServer(CloudServer cloudServer) {
        this.cloudServerMap.remove(cloudServer.getServerName());
    }

    public void removeServer(String name) {
        this.cloudServerMap.remove(name);
    }

    public CloudServer getServer(String name) {
        return this.cloudServerMap.get(name);
    }

    public boolean existServer(String name) {
        return this.cloudServerMap.containsKey(name);
    }
}