package com.bedrockcloud.bedrockcloud.server.privateserver;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public class PrivateGameServerProvider
{
    private final Map<String, PrivateGameServer> gameServerMap;

    public PrivateGameServerProvider() {
        this.gameServerMap = new HashMap<String, PrivateGameServer>();
    }
    
    public Map<String, PrivateGameServer> getGameServerMap() {
        return this.gameServerMap;
    }

    @ApiStatus.Internal
    public void addGameServer(final PrivateGameServer gameServer) {
        this.gameServerMap.put(gameServer.getServerName(), gameServer);
    }

    @ApiStatus.Internal
    public void removeServer(final PrivateGameServer gameServer) {
        this.gameServerMap.remove(gameServer.getServerName());
    }

    @ApiStatus.Internal
    public void removeServer(final String name) {
        this.gameServerMap.remove(name);
    }
    
    public PrivateGameServer getGameServer(final String name) {
        return this.gameServerMap.get(name);
    }
    
    public boolean existServer(final String name) {
        return this.gameServerMap.get(name) != null;
    }
}