package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

public class EndCommand extends Command implements Loggable
{
    public EndCommand() {
        super("end", "end", "Stop the cloud.");
    }

    @Override
    public void onCommand(final String[] args) {
        this.getLogger().info("§cStopped BedrockCloud");
        System.exit(0);
    }
}
