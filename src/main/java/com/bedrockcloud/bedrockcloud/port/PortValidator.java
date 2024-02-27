package com.bedrockcloud.bedrockcloud.port;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;

import java.net.*;
import java.util.HashSet;

public final class PortValidator {

    public static HashSet<Integer> ports = new HashSet<>();

    private static final int PORTS_BOUNCE_CLOUD = 30000;
    private static final int PORTS_BOUNCE_PROXY = 19132;
    private static final int PORTS_BOUNCE = 40000;

    public static int getFreeCloudPort() {
        var port = PORTS_BOUNCE_CLOUD;
        while (isCloudPortUsed(port) || isCloudPortUsed(port+1)) {
            port++;
        }

        return port;
    }

    public static int getNextServerPort(CloudServer server) {
        var port = PORTS_BOUNCE;
        if (server.getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
            while (isPortUsed(port) || isPortUsed(port + 1)) {
                port++;
            }
        }
        return port;
    }

    public static int getNextProxyServerPort(CloudServer server) {
        var port = PORTS_BOUNCE_PROXY;
        while ((isPortUsed(port) && isPortUsed(port+1)) && (getPorts().contains(port) && getPorts().contains(port+1))) {
            port++;
        }
        return port;
    }

    private static boolean isPortUsed(int port) {
        for (final var service : Cloud.getCloudServerProvider().getCloudServers().values()) {
            if (service.getServerPort() == port || service.getServerPort()+1 == port) return true;
        }

        try (final var serverSocket = new DatagramSocket(port)) {
            serverSocket.close();
            return false;
        } catch (Exception exception) {
            return true;
        }
    }

    private static boolean isCloudPortUsed(int port){
        try (final var serverSocket = new DatagramSocket(port)) {
            serverSocket.close();
            return false;
        } catch (Exception exception) {
            return true;
        }
    }

    public static HashSet<Integer> getPorts() {
        return ports;
    }
}
