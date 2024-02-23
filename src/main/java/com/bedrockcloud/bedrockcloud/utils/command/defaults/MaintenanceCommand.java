package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.bedrockcloud.bedrockcloud.utils.command.Command;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;

public class MaintenanceCommand extends Command implements Loggable {

    public MaintenanceCommand() {
        super("maintenance", "maintenance <add|remove|list> [name]", "Add or remove players to the maintenance list.");
    }

    @Override
    public void onCommand(final String[] args) {
        if (args.length == 0 || args.length > 2) {
            BedrockCloud.getLogger().command(this.getUsage());
            return;
        }

        String subcommand = args[0].toLowerCase();

        if (args.length == 1) {
            switch (subcommand) {
                case "list" -> listMaintenancePlayers();
                case "add", "remove" -> BedrockCloud.getLogger().command(this.getUsage());
            }
        } else {
            String playerName = args[1];
            switch (subcommand) {
                case "add" -> addMaintenancePlayer(playerName);
                case "remove" -> removeMaintenancePlayer(playerName);
            }
        }
    }

    private void listMaintenancePlayers() {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (String player : BedrockCloud.getMaintenanceFile().getAll().keySet()) {
            result.append(player).append(", ");
            ++count;
        }
        BedrockCloud.getLogger().command("§rCurrently are " + count + " players added to the maintenance list:");
        BedrockCloud.getLogger().command(result.length() > 0 ? result.substring(0, result.length() - 2) : "");
    }

    private void addMaintenancePlayer(String playerName) {
        if (!Utils.isMaintenance(playerName)) {
            Utils.addMaintenance(playerName);
            BedrockCloud.getLogger().command("§aThe player §e" + playerName + " §awas added to the maintenance list.");
        } else {
            BedrockCloud.getLogger().command("§cThe player §e" + playerName + " §cis already in the maintenance list.");
        }
    }

    private void removeMaintenancePlayer(String playerName) {
        if (Utils.isMaintenance(playerName)) {
            Utils.removeMaintenance(playerName);
            BedrockCloud.getLogger().command("§aThe player §e" + playerName + " §awas removed from the maintenance list.");
        } else {
            BedrockCloud.getLogger().command("§cThe player §e" + playerName + " §cis not in the maintenance list.");
        }
    }
}