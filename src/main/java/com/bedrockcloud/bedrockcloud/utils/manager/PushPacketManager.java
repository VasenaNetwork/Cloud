package com.bedrockcloud.bedrockcloud.utils.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class PushPacketManager {

    public static void pushPacket(final DataPacket cloudPacket, final CloudServer server) {
        if (server.getServerName() == null || server.getSocket() == null) {
            return;
        }

        if (server.getSocket().isClosed()) {
            BedrockCloud.getLogger().error("CloudPacket cannot be push because socket is closed.");
            return;
        }

        if (BedrockCloud.getNetworkManager().datagramSocket == null) {
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(cloudPacket.encode().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        try {
            InetAddress address;
            if (server.getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
                address = InetAddress.getByName("127.0.0.1");
            } else {
                address = InetAddress.getByName("0.0.0.0");
            }

            int port = server.getServerPort()+1;
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
            DatagramSocket datagramSocket = null;
            try {
                datagramSocket = new DatagramSocket();
            } catch (SocketException ex) {
                BedrockCloud.getLogger().exception(ex);
            }
            try {
                assert datagramSocket != null;
                datagramSocket.send(datagramPacket);
            } catch (IOException ex) {
                BedrockCloud.getLogger().exception(ex);
            }
        } catch (UnknownHostException ignored) {
        }
    }
}
