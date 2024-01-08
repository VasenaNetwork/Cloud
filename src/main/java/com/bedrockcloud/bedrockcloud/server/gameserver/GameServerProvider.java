package com.bedrockcloud.bedrockcloud.server.gameserver;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public class GameServerProvider
{
    @ApiStatus.Internal
    private final Map<String, GameServer> gameServerMap;
    
    public GameServerProvider() {
        this.gameServerMap = new HashMap<>();
    }
    
    public Map<String, GameServer> getGameServerMap() {
        return this.gameServerMap;
    }

    @ApiStatus.Internal
    public void addGameServer(final GameServer gameServer) {
        this.gameServerMap.put(gameServer.getServerName(), gameServer);
    }

    @ApiStatus.Internal
    public void removeServer(final GameServer gameServer) {
        this.gameServerMap.remove(gameServer.getServerName());
    }

    @ApiStatus.Internal
    public void removeServer(final String name) {
        this.gameServerMap.remove(name);
    }
    
    public GameServer getGameServer(final String name) {
        return this.gameServerMap.get(name);
    }
    
    public boolean existServer(final String name) {
        return this.gameServerMap.get(name) != null;
    }
}
