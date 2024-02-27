package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.Cloud;
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
            Cloud.getLogger().warning("Try to execute: " + this.getUsage());
            return;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "list" -> listServers();
            case "start" -> startServer(args);
            case "stop" -> stopServer(args);
            case "save" -> saveServer(args);
            default -> Cloud.getLogger().warning("Try to execute: " + this.getUsage());
        }
    }

    private void listServers() {
        int services = Cloud.getCloudServerProvider().getCloudServers().size();
        Cloud.getLogger().info("§e»§r §7There are currently " + services + " Services online! §e«");
        for (final CloudServer server : Cloud.getCloudServerProvider().getCloudServers().values()) {
            Cloud.getLogger().info("§c➤ §rServer: " + server.getServerName() + " | UUID: " + server.getUuid() + " | Players: " + server.getPlayerCount() + " ᐅ " + server.getTemplate().getName() + " | Static: " + Utils.boolToString(server.getTemplate().isStatic()));
        }
    }

    private void startServer(String[] args) {
        if (args.length != 3) {
            Cloud.getLogger().warning("Try to execute: server start <group> <count>");
            return;
        }

        final String groupName = args[1];
        final int count = Integer.parseInt(args[2]);
        final Template group = Cloud.getTemplateProvider().getTemplate(groupName);

        if (group == null) {
            Cloud.getLogger().error("This Group doesn't exist");
            return;
        }

        if (!Cloud.getTemplateProvider().isTemplateRunning(group)) {
            Cloud.getLogger().error("The Group is currently not running!");
            return;
        }

        for (int i = 0; i < count; ++i) {
            new CloudServer(group);
        }
    }

    private void stopServer(String[] args) {
        if (args.length != 2) {
            Cloud.getLogger().warning("Try to execute: server stop <server>");
            return;
        }

        final String servername = args[1];
        final CloudServer server = Cloud.getCloudServerProvider().getServer(servername);

        if (server == null) {
            Cloud.getLogger().error("The Server doesn't exist!");
            return;
        }

        server.stopServer();
    }

    private void saveServer(String[] args) {
        if (args.length != 2) {
            Cloud.getLogger().warning("Try to execute: server save <server>");
            return;
        }

        final String servername = args[1];
        final CloudServer server = Cloud.getCloudServerProvider().getServer(servername);

        if (server == null) {
            Cloud.getLogger().error("The Server doesn't exist!");
            return;
        }

        server.saveServer();
    }
}