package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.command.Command;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.manager.MaintenanceManager;

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

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "list" -> {
                    StringBuilder result = new StringBuilder();
                    int count = 0;
                    for (String player : BedrockCloud.getMaintenanceFile().getAll().keySet()) {
                        result.append(player).append(", ");
                        ++count;
                    }
                    BedrockCloud.getLogger().command("§rCurrently are " + count + " players added to the the maintenance list:");
                    BedrockCloud.getLogger().command(result.length() > 0 ? result.substring(0, result.length() - 2) : "");
                }
                case "add" -> {
                    BedrockCloud.getLogger().command(this.getUsage());
                }
                case "remove" -> BedrockCloud.getLogger().command(this.getUsage());
            }
        } else {
            switch (args[0].toLowerCase()) {
                case "add" -> {
                    if (!MaintenanceManager.isMaintenance(args[1])) {
                        MaintenanceManager.addMaintenance(args[1]);
                        BedrockCloud.getLogger().command("§aThe player §e" + args[1] + " §awas added to the maintenance list.");
                    } else {
                        BedrockCloud.getLogger().command("§cThe player §e" + args[1] + " §cis already in the maintenance list.");
                    }
                }
                case "remove" -> {
                    if (MaintenanceManager.isMaintenance(args[1])) {
                        MaintenanceManager.addMaintenance(args[1]);
                        BedrockCloud.getLogger().command("§aThe player §e" + args[1] + " §awas removed from the maintenance list.");
                    } else {
                        BedrockCloud.getLogger().command("§cThe player §e" + args[1] + " §cis not in the maintenance list.");
                    }
                }
            }
        }
    }
}