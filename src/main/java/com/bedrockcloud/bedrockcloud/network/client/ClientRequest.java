package com.bedrockcloud.bedrockcloud.network.client;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientRequest extends Thread implements AutoCloseable {
    private final DatagramPacket datagramPacket;
    private final DatagramSocket socket;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;

    public ClientRequest(final DatagramPacket datagramPacket, final DatagramSocket socket) {
        this.datagramPacket = datagramPacket;
        this.socket = socket;
        this.dataInputStream = new DataInputStream(new ByteArrayInputStream(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength()));
        this.dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
    }

    public DatagramPacket getDatagramPacket() {
        return this.datagramPacket;
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    @Override
    public void run() {
        try {
            String line = this.dataInputStream.readLine();
            if (line != null) {
                try {
                    String packetName = BedrockCloud.getPacketHandler().getPacketNameByRequest(line);
                    JSONObject jsonObject = BedrockCloud.getPacketHandler().handleJsonObject(packetName, line);
                    BedrockCloud.getPacketHandler().handleCloudPacket(jsonObject, this);
                } catch (NullPointerException ex) {
                    BedrockCloud.getLogger().exception(ex);
                }
            }
        } catch (IOException ex) {
            BedrockCloud.getLogger().exception(ex);
        } finally {
            try {
                this.dataOutputStream.flush();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] data = byteArrayOutputStream.toByteArray();
                DatagramPacket responsePacket = new DatagramPacket(data, data.length, this.datagramPacket.getAddress(), this.datagramPacket.getPort());
                DatagramSocket datagramSocket = new DatagramSocket();
                datagramSocket.send(responsePacket);
                datagramSocket.close();
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.dataOutputStream.close();
        this.dataInputStream.close();
    }
}