package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

public class HelpCommand extends Command implements Loggable
{
    public HelpCommand() {
        super("help", "help", "List all commands from the cloud.");
    }
    
    @Override
    public void onCommand(final String[] args) {
        StringBuilder message = new StringBuilder("§aList of all commands:§r\n");
        for (Command cmd : Cloud.getConsoleReader().getCommands()){
            message.append("§8| §eCommand§8: §e").append(cmd.getCommand()).append(" §8| §eUsage§8: §e").append(cmd.getUsage()).append(" §8| §eDescription§8: §e").append(cmd.getDescription()).append("§r\n");
        }
        Cloud.getLogger().command(message.toString());
    }
}
