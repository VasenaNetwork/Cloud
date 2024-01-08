package com.bedrockcloud.bedrockcloud.utils.command;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.command.defaults.*;

public class CommandRegistry {
    public void registerAllCommands() {
        BedrockCloud.getConsoleReader().addCommand(new HelpCommand());
        BedrockCloud.getConsoleReader().addCommand(new ServerCommand());
        BedrockCloud.getConsoleReader().addCommand(new TemplateCommand());
        BedrockCloud.getConsoleReader().addCommand(new SoftwareCommand());
        BedrockCloud.getConsoleReader().addCommand(new PlayerCommand());
        BedrockCloud.getConsoleReader().addCommand(new EndCommand());
        BedrockCloud.getConsoleReader().addCommand(new InfoCommand());
        BedrockCloud.getConsoleReader().addCommand(new MaintenanceCommand());
    }

    public void registerCommand(Command command) {
        if (BedrockCloud.getConsoleReader().getCommand(command.getCommand()) == null) {
            BedrockCloud.getConsoleReader().addCommand(command);
        }
    }
}
