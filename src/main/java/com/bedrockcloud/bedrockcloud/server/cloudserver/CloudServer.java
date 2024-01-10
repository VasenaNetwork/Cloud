package com.bedrockcloud.bedrockcloud.server.cloudserver;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.api.MessageAPI;
import com.bedrockcloud.bedrockcloud.api.event.server.ServerStartEvent;
import com.bedrockcloud.bedrockcloud.api.event.server.ServerStopEvent;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.packets.GameServerDisconnectPacket;
import com.bedrockcloud.bedrockcloud.network.packets.proxy.ProxyServerDisconnectPacket;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.server.properties.ServerProperties;
import com.bedrockcloud.bedrockcloud.tasks.KeepALiveTask;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceHelper.ServiceHelper;
import com.bedrockcloud.bedrockcloud.utils.helper.serviceKiller.ServiceKiller;
import com.bedrockcloud.bedrockcloud.utils.manager.CloudNotifyManager;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.utils.manager.PushPacketManager;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.CompletableFuture;

public class CloudServer {
    private static final int TIMEOUT = 20;

    @Getter
    private final Template template;
    @Getter
    private final String serverName;
    @Getter
    private final int serverPort;
    @Setter
    @Getter
    private int pid;
    @Setter
    @Getter
    private int state;
    @Setter
    @Getter
    private int playerCount;
    @Setter
    @Getter
    private int aliveChecks;
    @Setter
    @Getter
    private DatagramSocket socket;
    @Setter
    @Getter
    private KeepALiveTask task = null;
    @Setter
    @Getter
    private boolean isConnected = false;
    private final long startTime;

    public CloudServer(final Template template) {
        this.template = template;
        this.aliveChecks = 0;
        this.serverName = template.getName() + Utils.getServiceSeperator() + FileManager.getFreeNumber("./temp/" + template.getName());
        if (getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
            this.serverPort = PortValidator.getNextServerPort(this);
        } else {
            this.serverPort = PortValidator.getNextProxyServerPort(this);
        }
        this.playerCount = 0;
        this.state = 0;
        this.pid = -1;

        this.startTime = System.currentTimeMillis() / 1000;

        ServiceKiller.killPid(this);
        BedrockCloud.getCloudServerProvider().addServer(this);

        ServerStartEvent event = new ServerStartEvent(this);
        BedrockCloud.getInstance().getPluginManager().callEvent(event);

        this.copyServer();
        try {
            this.startServer();
        } catch (InterruptedException e) {
            BedrockCloud.getLogger().exception(e);
        }
        this.checkAliveAsync();
    }

    private void checkAliveAsync() {
        CompletableFuture.supplyAsync(() -> {
            while (!this.isConnected() && BedrockCloud.getCloudServerProvider().existServer(this.getServerName())) {
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

    private boolean checkAlive(){
        long currentTime = System.currentTimeMillis() / 1000;

        if ((currentTime - this.startTime) < TIMEOUT) return true;
        if (this.isConnected()) return true;
        return false;
    }

    private void startServer() throws InterruptedException {
        final File server = new File("./temp/" + this.serverName);
        if (server.exists()) {
            final ProcessBuilder builder = new ProcessBuilder();

            String notifyMessage = MessageAPI.startMessage.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().info(notifyMessage);
            try {
                builder.command("/bin/sh", "-c", "screen -X -S " + this.serverName + " kill").start();
            } catch (Exception e) {
                BedrockCloud.getLogger().exception(e);
            }

            if (getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
                try {
                    builder.command("/bin/sh", "-c", "screen -dmS " + this.serverName + " ../../bin/php7/bin/php ../../local/versions/pocketmine/PocketMine-MP.phar").directory(new File("./temp/" + this.serverName)).start();
                } catch (Exception e) {
                    BedrockCloud.getLogger().exception(e);
                }
            } else {
                try {
                    builder.command("/bin/sh", "-c", "screen -dmS " + this.serverName + " java -jar ../../local/versions/waterdogpe/WaterdogPE.jar").directory(new File("./temp/" + this.serverName)).start();
                } catch (Exception e) {
                    BedrockCloud.getLogger().exception(e);
                }
            }

            PortValidator.ports.add(this.getServerPort());
            PortValidator.ports.add(this.getServerPort()+1);
        } else {
            String notifyMessage = MessageAPI.startFailed.replace("%service", serverName);
            CloudNotifyManager.sendNotifyCloud(notifyMessage);
            BedrockCloud.getLogger().error(notifyMessage);

            PortValidator.ports.remove(this.getServerPort());
            PortValidator.ports.remove(this.getServerPort()+1);
        }
    }

    private void copyServer() {
        final File src = new File("./templates/" + this.template.getName() + "/");
        final File dest = new File("./temp/" + this.serverName);
        if (!dest.exists()) {
            FileManager.copy(src, dest);
            ServerProperties.createProperties(this);
        }

        if (getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
            final File global_plugins = new File("./local/plugins/pocketmine");
            final File dest_plugs = new File("./temp/" + this.serverName + "/plugins/");
            FileManager.copy(global_plugins, dest_plugs);
        } else {
            final File global_plugins = new File("./local/plugins/waterdogpe");
            final File dest_plugs = new File("./temp/" + this.serverName + "/plugins/");
            FileManager.copy(global_plugins, dest_plugs);
            final Config config = new Config("./temp/" + this.serverName + "/cloud.yml", Config.YAML);
            config.set("name", this.serverName);
            config.save();
        }

        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        ServerStopEvent event = new ServerStopEvent(this);
        BedrockCloud.getInstance().getPluginManager().callEvent(event);

        String notifyMessage = MessageAPI.stopMessage.replace("%service", this.serverName);
        CloudNotifyManager.sendNotifyCloud(notifyMessage);
        BedrockCloud.getLogger().info(notifyMessage);

        if (getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
            final GameServerDisconnectPacket packet = new GameServerDisconnectPacket();
            packet.addValue("reason", "Server Stopped");
            this.pushPacket(packet);
        } else {
            final ProxyServerDisconnectPacket packet = new ProxyServerDisconnectPacket();
            packet.addValue("reason", "Proxy Stopped");
            this.pushPacket(packet);
        }
    }

    public void pushPacket(final DataPacket cloudPacket) {
        PushPacketManager.pushPacket(cloudPacket, this);
    }

    @Override
    public String toString() {
        return "CloudServer{template=" + this.template + ", serverName='" + this.serverName + '\'' + ", serverPort=" + this.serverPort + ", playerCount=" + this.playerCount + ", aliveChecks=" + this.aliveChecks + ", socket=" + this.socket + ", temp_path='" + "./templates/" + '\'' + ", servers_path='" + "./temp/" + '\'' + '}';
    }
}
