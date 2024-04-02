package com.bedrockcloud.bedrockcloud.network;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.network.client.ClientRequest;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

@ApiStatus.Internal
public class NetworkManager implements Loggable {
    private final DatagramSocket socket;

    public NetworkManager(final int port) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            socket.setReuseAddress(true);
            getLogger().info("Listening on 127.0.0.1:" + port);
        } catch (IOException e) {
            getLogger().exception(e);
        }
        this.socket = socket;
    }

    public void start() {
        while (Cloud.isRunning()) {
            if (this.socket != null && !this.socket.isClosed()) {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                    this.socket.receive(datagramPacket);

                    if (datagramPacket.getLength() > 0) {
                        ClientRequest request = new ClientRequest(datagramPacket, this.socket);
                        request.start();
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }

    public DatagramSocket getCloudSocket() {
        return socket;
    }
}