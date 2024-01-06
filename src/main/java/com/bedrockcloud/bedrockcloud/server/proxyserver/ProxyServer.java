package com.bedrockcloud.bedrockcloud.server.proxyserver;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.utils.manager.PushPacketManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.properties.ServerProperties;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceKiller.ServiceKiller;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;

import java.io.*;

import com.bedrockcloud.bedrockcloud.network.packets.proxy.ProxyServerDisconnectPacket;
import com.bedrockcloud.bedrockcloud.utils.Utils;

import java.net.*;
import java.util.concurrent.CompletableFuture;

public class ProxyServer {
    private static final int TIMEOUT = 20;

    private final Template template;
    private final String serverName;
    private final int serverPort;
    private DatagramSocket socket;
    public int pid;
    public int socketPort;
    private boolean isConnected = false;
    private final long startTime;

    public ProxyServer(final Template template) {
        this.template = template;
        this.serverName = template.getName() + Utils.getServiceSeperator() + FileManager.getFreeNumber("./temp/" + template.getName());
        this.serverPort = PortValidator.getNextProxyServerPort(this);
        this.pid = -1;
        this.socketPort = -1;
        this.startTime = System.currentTimeMillis() / 1000;

        ServiceKiller.killPid(this);

        BedrockCloud.getProxyServerProvider().addProxyServer(this);
        this.copyServer();
        try {
            this.startServer();
        } catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
        this.checkAliveAsync();
    }

    public CompletableFuture<Boolean> checkAliveAsync() {
        return CompletableFuture.supplyAsync(() -> {
            while (!this.isConnected() && BedrockCloud.getProxyServerProvider().existServer(this.getServerName())) {
                if (!checkAlive()) {
                    String servername = getServerName();

                    String notifyMessage = MessageAPI.startFailed.replace("%service", servername);
                    CloudNotifyManager.sendNotifyCloud(notifyMessage);
                    BedrockCloud.getLogger().warning(notifyMessage);

                    try {
                        PortValidator.ports.remove(getServerPort());
                        PortValidator.ports.remove(getServerPort() + 1);

                        if (BedrockCloud.getTemplateProvider().isTemplateRunning(getTemplate())) {
                            ServiceHelper.killWithPID(this);
                        } else {
                            ServiceHelper.killWithPID(false, this);
                        }
                    } catch (IOException exception) {
                        BedrockCloud.getLogger().exception(exception);
                    }
                    return false;
                }
            }
            return true;
        });
    }

    public boolean checkAlive(){
        long currentTime = System.currentTimeMillis() / 1000;

        if ((currentTime - this.startTime) < TIMEOUT) return true;
        if (this.isConnected()) return true;
        return false;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public Template getTemplate() {
        return this.template;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    public void setSocket(final DatagramSocket socket) {
        this.socket = socket;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public void startServer() throws InterruptedException {
        final File server = new File("./temp/" + this.serverName);
        if (server.exists()) {
            final ProcessBuilder builder = new ProcessBuilder(new String[0]);

            String notifyMessage = MessageAPI.startMessage.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            try {
                builder.command("/bin/sh", "-c", "screen -X -S " + this.serverName + " kill").start();
            } catch (Exception e) {
                BedrockCloud.getLogger().exception(e);
            }

            try {
                builder.command("/bin/sh", "-c", "screen -dmS " + this.serverName + " java -jar ../../local/versions/waterdogpe/WaterdogPE.jar").directory(new File("./temp/" + this.serverName)).start();
            } catch (Exception e) {
                BedrockCloud.getLogger().exception(e);
            }

            PortValidator.ports.add(this.getServerPort());
            PortValidator.ports.add(this.getServerPort()+1);
        } else {
            String notifyMessage = MessageAPI.startFailed.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            PortValidator.ports.remove(this.getServerPort());
            PortValidator.ports.remove(this.getServerPort()+1);
        }
    }

    public void stopServer() {
        String notifyMessage = MessageAPI.stopMessage.replace("%service", this.serverName);
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        final ProxyServerDisconnectPacket packet = new ProxyServerDisconnectPacket();
        packet.addValue("reason", "Proxy Stopped");
        this.pushPacket(packet);
    }

    public void pushPacket(final DataPacket cloudPacket) {
        PushPacketManager.pushPacket(cloudPacket, this);
    }

    public void copyServer() {
        final File src = new File("./templates/" + this.template.getName() + "/");
        final File dest = new File("./temp/" + this.serverName);
        if (!dest.exists()) {
            FileManager.copy(src, dest);
            ServerProperties.createProperties(this);
        }
        final File global_plugins = new File("./local/plugins/waterdogpe");
        final File dest_plugs = new File("./temp/" + this.serverName + "/plugins/");
        FileManager.copy(global_plugins, dest_plugs);
        final Config config = new Config("./temp/" + this.serverName + "/cloud.yml", Config.YAML);
        config.set("name", this.serverName);
        config.save();
    }
}
