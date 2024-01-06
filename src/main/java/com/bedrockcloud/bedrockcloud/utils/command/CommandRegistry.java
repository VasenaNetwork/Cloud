package com.bedrockcloud.bedrockcloud.utils.command;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.command.defaults.*;

public class CommandRegistry {

    public static void registerCommands() {
        BedrockCloud.getConsoleReader().addCommand(new HelpCommand());
        BedrockCloud.getConsoleReader().addCommand(new ServerCommand());
        BedrockCloud.getConsoleReader().addCommand(new TemplateCommand());
        BedrockCloud.getConsoleReader().addCommand(new SoftwareCommand());
        BedrockCloud.getConsoleReader().addCommand(new PlayerCommand());
        BedrockCloud.getConsoleReader().addCommand(new EndCommand());
        BedrockCloud.getConsoleReader().addCommand(new InfoCommand());
        BedrockCloud.getConsoleReader().addCommand(new MaintenanceCommand());
    }
}
