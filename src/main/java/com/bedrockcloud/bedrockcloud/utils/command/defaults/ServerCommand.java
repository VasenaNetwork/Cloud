package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.manager.FileManager;
import com.bedrockcloud.bedrockcloud.templates.Template;
import java.io.File;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.command.Command;
import com.bedrockcloud.bedrockcloud.utils.Utils;

public class ServerCommand extends Command
{
    public ServerCommand() {
        super("server", "server <start | stop | save | list> <group | serverName> <count>", "Manage servers");
    }
    
    @Override
    public void onCommand(final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                int services = BedrockCloud.getCloudServerProvider().getCloudServers().size();
                this.getLogger().info("§e»§r §7There are currently " + services + " Services online! §e«");
                for (final CloudServer server : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
                    this.getLogger().info("§c➤ §rServer: " + server.getServerName() + " | UUID: " + server.getUuid() + " | Players: " + server.getPlayerCount() + " ᐅ " + server.getTemplate().getName() + " | Static: " + Utils.boolToString(server.getTemplate().getStatic()));
                }
            } else if (args.length > 1) {
                if (args[0].equalsIgnoreCase("start")) {
                    if (args.length == 3) {
                        final String groupName = args[1];
                        final int count = Integer.parseInt(args[2]);
                        final Template group = BedrockCloud.getTemplateProvider().getTemplate(groupName);
                        if (group == null) {
                            BedrockCloud.getLogger().error("This Group doesn't exist");
                            return;
                        }

                        if (!BedrockCloud.getTemplateProvider().isTemplateRunning(group)) {
                            BedrockCloud.getLogger().error("The Group is currently not running!");
                            return;
                        }

                        for (int i = 0; i < count; ++i) {
                            new CloudServer(group);
                        }
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: server start <group> <count>");
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (args.length == 2) {
                        final String servername = args[1];
                        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(servername);
                        if (server != null) {
                            server.stopServer();
                        }
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: server stop <server>");
                    }
                } else if (args[0].equalsIgnoreCase("save")) {
                    final String servername = args[1];
                    final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(servername);
                    if (server == null){
                        BedrockCloud.getLogger().error("The Server doesn't exist!");
                        return;
                    }

                    server.saveServer();
                }
            }
        } else {
            BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
        }
    }
}
