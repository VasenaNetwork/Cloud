package com.bedrockcloud.bedrockcloud.templates;

import java.util.HashMap;
import java.util.Objects;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import org.jetbrains.annotations.ApiStatus;

public class Template implements Loggable
{
    private final String name;
    private final int minRunningServer;
    private final int maxRunningServer;
    private final int maxPlayers;
    private final int type;
    public final Boolean isBeta;
    public final Boolean isMaintenance;
    public final Boolean isLobby;
    public final Boolean isStatic;
    public HashMap<String, Template> runningTemplateServers;
    public HashMap<String, String> templatePlayer;
    
    public Template(final String name, final Integer minRunningServer, final Integer maxRunningServer, final Integer maxPlayers, final Integer type, final Boolean isBeta, final Boolean isMaintenance, final Boolean isLobby, final Boolean isStatic) {
        this.name = name;
        this.minRunningServer = Math.round(minRunningServer);
        this.maxRunningServer = Math.round(maxRunningServer);
        this.maxPlayers = Math.round(maxPlayers);
        this.type = type;
        this.isBeta = isBeta;
        this.isMaintenance = isMaintenance;
        this.isLobby = isLobby;
        this.isStatic = isStatic;
        if (!BedrockCloud.getTemplateProvider().existsTemplate(this.getName())) {
            BedrockCloud.getTemplateProvider().addTemplate(this);
        }
        this.runningTemplateServers = new HashMap<String, Template>();
        this.templatePlayer = new HashMap<String, String>();
    }

    public HashMap<String, Template> getRunningTemplateServers() {
        return this.runningTemplateServers;
    }

    @ApiStatus.Internal
    public void addServer(final Template template, final String serverName) {
        this.runningTemplateServers.put(serverName, template);
    }

    @ApiStatus.Internal
    public void removeServer(final String name) {
        this.runningTemplateServers.remove(name);
    }

    public HashMap<String, String> getTemplatePlayers() {
        return this.templatePlayer;
    }

    @ApiStatus.Internal
    public void addPlayer(final CloudPlayer player, final String serverName) {
        this.templatePlayer.put(serverName, player.getPlayerName());
    }

    @ApiStatus.Internal
    public void removePlayer(final CloudPlayer name) {
        this.templatePlayer.remove(name.getPlayerName());
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getCurrentPlayers() {
        return this.templatePlayer.size();
    }
    
    public int getMaxRunningServer() {
        return this.maxRunningServer;
    }
    
    public int getMinRunningServer() {
        return this.minRunningServer;
    }
    
    public int getType() {
        return this.type;
    }

    public void start(boolean force) {
        if (this.getMaintenance()) {
            if (!force) {
                BedrockCloud.getLogger().warning("§cThe group §e" + this.getName() + " §cwas not started because it is in maintenance, but you can start it yourself with the command §7'§etemplate start <template>§7'§c.");
                return;
            }
        }

        BedrockCloud.getLogger().info("Starting group " + this.getName() + "...");
        for (int i = 0; i < this.getMinRunningServer(); ++i) {
            new CloudServer(this);
        }
        BedrockCloud.getTemplateProvider().addRunningTemplate(this);
    }

    public void restart() {
        BedrockCloud.getLogger().info("Restarting group " + this.getName() + "...");
        for (final String servername : BedrockCloud.getCloudServerProvider().getCloudServers().keySet()) {
            if (Objects.equals(BedrockCloud.getCloudServerProvider().getServer(servername).getTemplate().getName(), this.getName())) {
                final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }
    }
    
    public void stop() {
        BedrockCloud.getLogger().info("Stopping group " + this.getName() + "...");
        BedrockCloud.getTemplateProvider().removeRunningGroup(this);

        for (final String servername : BedrockCloud.getCloudServerProvider().getCloudServers().keySet()) {
            if (Objects.equals(BedrockCloud.getCloudServerProvider().getServer(servername).getTemplate().getName(), this.getName())) {
                final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(servername);
                if (server == null) {
                    return;
                }
                server.stopServer();
            }
        }
    }
    
    public Boolean getBeta() {
        return this.isBeta;
    }
    
    public Boolean getLobby() {
        return this.isLobby;
    }
    
    public Boolean getMaintenance() {
        return this.isMaintenance;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    @Override
    public String toString() {
        return "Template{name='" + this.name + '\'' + ", minRunningServer=" + this.minRunningServer + ", maxRunningServer=" + this.maxRunningServer + ", maxPlayers=" + this.maxPlayers + ", type='" + this.type + '\'' + '}';
    }
}
