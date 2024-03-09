package com.bedrockcloud.bedrockcloud.utils.command;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.utils.command.defaults.*;
import org.jetbrains.annotations.ApiStatus;

public class CommandRegistry {

    @ApiStatus.Internal
    public static void registerAllCommands() {
        Cloud.getConsoleReader().addCommand(new HelpCommand());
        Cloud.getConsoleReader().addCommand(new ServerCommand());
        Cloud.getConsoleReader().addCommand(new TemplateCommand());
        Cloud.getConsoleReader().addCommand(new SoftwareCommand());
        Cloud.getConsoleReader().addCommand(new PlayerCommand());
        Cloud.getConsoleReader().addCommand(new EndCommand());
        Cloud.getConsoleReader().addCommand(new InfoCommand());
        Cloud.getConsoleReader().addCommand(new MaintenanceCommand());
    }

    public boolean registerCommand(Command command) throws Exception {
        if (Cloud.getConsoleReader().getCommand(command.getCommand()) == null) {
            Cloud.getConsoleReader().addCommand(command);
            return true;
        }
        return false;
    }

    public boolean unregisterCommand(Command command) {
        if (Cloud.getConsoleReader().getCommand(command.getCommand()) != null) {
            Cloud.getConsoleReader().removeCommand(command);
            return true;
        }
        return false;
    }
}
