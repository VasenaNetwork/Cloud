package com.bedrockcloud.bedrockcloud.server.privateserver;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.utils.manager.PushPacketManager;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.packets.GameServerDisconnectPacket;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.properties.ServerProperties;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceKiller.ServiceKiller;
import com.bedrockcloud.bedrockcloud.tasks.PrivateKeepALiveTask;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.utils.Utils;

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class PrivateGameServer {
    private static final int TIMEOUT = 20;

    private final Template template;
    private final String serverName;
    private final int serverPort;
    public int pid;
    public int state;
    private int playerCount;
    private int aliveChecks;
    private DatagramSocket socket;
    public String serverOwner;
    public PrivateKeepALiveTask task = null;
    private boolean isConnected = false;
    private final long startTime;

    public PrivateGameServer(final Template template, String serverOwner) {
        this.template = template;
        this.aliveChecks = 0;
        this.serverName = template.getName() + Utils.getServiceSeperator() + FileManager.getFreeNumber("./temp/" + template.getName());
        this.serverPort = PortValidator.getNextPrivateServerPort(this);
        this.playerCount = 0;
        this.state = 0;
        this.pid = -1;
        this.startTime = System.currentTimeMillis() / 1000;

        ServiceKiller.killPid(this);

        BedrockCloud.getPrivategameServerProvider().addGameServer(this);
        this.copyServer();
        this.serverOwner = serverOwner;
        try {
            this.startServer();
        } catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
        this.checkAliveAsync();
    }

    public CompletableFuture<Boolean> checkAliveAsync() {
        return CompletableFuture.supplyAsync(() -> {
            while (!this.isConnected() && BedrockCloud.getPrivategameServerProvider().existServer(this.getServerName())) {
                if (!checkAlive()) {
                    String servername = getServerName();
                    this.setAliveChecks(0);

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

    public PrivateKeepALiveTask getTask() {
        return task;
    }

    public int getPid() {
        return this.pid;
    }
    
    public String getServerName() {
        return this.serverName;
    }

    public String getServerOwner(){
        return this.serverOwner;
    }
    
    public int getServerPort() {
        return this.serverPort;
    }
    
    public int getAliveChecks() {
        return this.aliveChecks;
    }
    
    public void setAliveChecks(final int aliveChecks) {
        this.aliveChecks = aliveChecks;
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
                builder.command("/bin/sh", "-c", "screen -dmS " + this.serverName + " ../../bin/php7/bin/php ../../local/versions/pocketmine/PocketMine-MP.phar").directory(new File("./temp/" + this.serverName)).start();
            } catch (Exception e) {
                BedrockCloud.getLogger().exception(e);
            }

            PortValidator.ports.add(this.getServerPort());
            PortValidator.ports.add(this.getServerPort()+1);
        } else {
            String notifyMessage = MessageAPI.startFailed.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().warning(notifyMessage);

            if (BedrockCloud.getCloudPlayerProvider().existsPlayer(getServerOwner())) {
                final PlayerTextPacket playerTextPacket = new PlayerTextPacket();
                playerTextPacket.playerName = getServerOwner();
                Objects.requireNonNull(playerTextPacket);
                playerTextPacket.type = 0;
                playerTextPacket.value = BedrockCloud.prefix + "§cYour server can't be started, please try again.";
                BedrockCloud.getCloudPlayerProvider().getCloudPlayer(getServerName()).getProxy().pushPacket(playerTextPacket);
            }

            PortValidator.ports.remove(this.getServerPort());
            PortValidator.ports.remove(this.getServerPort()+1);
        }
    }
    
    public void copyServer() {
        final File src = new File("./templates/" + this.template.getName() + "/");
        final File dest = new File("./temp/" + this.serverName);
        if (!dest.exists()) {
            FileManager.copy(src, dest);
            ServerProperties.createProperties(this);
        }
        final File global_plugins = new File("./local/plugins/pocketmine");
        final File dest_plugs = new File("./temp/" + this.serverName + "/plugins/");
        FileManager.copy(global_plugins, dest_plugs);

        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public Template getTemplate() {
        return this.template;
    }
    
    public void stopServer() {
        String notifyMessage = MessageAPI.stopMessage.replace("%service", this.serverName);
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().warning(notifyMessage);

        final GameServerDisconnectPacket packet = new GameServerDisconnectPacket();
        packet.addValue("reason", "Server Stopped");
        this.pushPacket(packet);
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    public void setSocket(final DatagramSocket socket) {
        this.socket = socket;
    }

    public void pushPacket(final DataPacket cloudPacket) {
        PushPacketManager.pushPacket(cloudPacket, this);
    }
    
    public int getPlayerCount() {
        return this.playerCount;
    }
    
    public void setPlayerCount(final int v) {
        this.playerCount = v;
    }
    
    public int getState() {
        return this.state;
    }
    
    @Override
    public String toString() {
        return "PrivateGameServer{template=" + this.template + ", serverName='" + this.serverName + '\'' + ", serverPort=" + this.serverPort + ", playerCount=" + this.playerCount + ", aliveChecks=" + this.aliveChecks + ", socket=" + this.socket + ", temp_path='" + "./templates/" + '\'' + ", servers_path='" + "./temp/" + '\'' + '}';
    }
}
