package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

public class ServerCommand extends Command {

    public ServerCommand() {
        super("server", "server <start | stop | save | list> <group | serverName> <count>", "Manage servers");
    }

    @Override
    public void onCommand(final String[] args) {
        if (args.length == 0) {
            BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
            return;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "list" -> listServers();
            case "start" -> startServer(args);
            case "stop" -> stopServer(args);
            case "save" -> saveServer(args);
            default -> BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
        }
    }

    private void listServers() {
        int services = BedrockCloud.getCloudServerProvider().getCloudServers().size();
        BedrockCloud.getLogger().info("§e»§r §7There are currently " + services + " Services online! §e«");
        for (final CloudServer server : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            BedrockCloud.getLogger().info("§c➤ §rServer: " + server.getServerName() + " | UUID: " + server.getUuid() + " | Players: " + server.getPlayerCount() + " ᐅ " + server.getTemplate().getName() + " | Static: " + Utils.boolToString(server.getTemplate().getStatic()));
        }
    }

    private void startServer(String[] args) {
        if (args.length != 3) {
            BedrockCloud.getLogger().warning("Try to execute: server start <group> <count>");
            return;
        }

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
    }

    private void stopServer(String[] args) {
        if (args.length != 2) {
            BedrockCloud.getLogger().warning("Try to execute: server stop <server>");
            return;
        }

        final String servername = args[1];
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(servername);

        if (server == null) {
            BedrockCloud.getLogger().error("The Server doesn't exist!");
            return;
        }

        server.stopServer();
    }

    private void saveServer(String[] args) {
        if (args.length != 2) {
            BedrockCloud.getLogger().warning("Try to execute: server save <server>");
            return;
        }

        final String servername = args[1];
        final CloudServer server = BedrockCloud.getCloudServerProvider().getServer(servername);

        if (server == null) {
            BedrockCloud.getLogger().error("The Server doesn't exist!");
            return;
        }

        server.saveServer();
    }
}