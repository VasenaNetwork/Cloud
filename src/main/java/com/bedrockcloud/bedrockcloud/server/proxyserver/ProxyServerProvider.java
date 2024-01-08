package com.bedrockcloud.bedrockcloud.server.proxyserver;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public class ProxyServerProvider {
    @ApiStatus.Internal
    private final Map<String, ProxyServer> proxyServerMap;
    
    public ProxyServerProvider() {
        this.proxyServerMap = new HashMap<>();
    }
    
    public Map<String, ProxyServer> getProxyServerMap() {
        return this.proxyServerMap;
    }

    @ApiStatus.Internal
    public void addProxyServer(final ProxyServer proxyServer) {
        this.proxyServerMap.put(proxyServer.getServerName(), proxyServer);
    }

    @ApiStatus.Internal
    public void removeServer(final ProxyServer proxyServer) {
        this.proxyServerMap.remove(proxyServer.getServerName());
    }

    @ApiStatus.Internal
    public void removeServer(final String name) {
        this.proxyServerMap.remove(name);
    }
    
    public ProxyServer getProxyServer(final String name) {
        return this.proxyServerMap.get(name);
    }
    
    public boolean existServer(final String name) {
        return this.proxyServerMap.get(name) != null;
    }
}
