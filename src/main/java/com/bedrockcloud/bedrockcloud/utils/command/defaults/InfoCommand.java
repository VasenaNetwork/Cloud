package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.command.Command;
import com.bedrockcloud.bedrockcloud.utils.Utils;

public class InfoCommand extends Command implements Loggable
{
    public InfoCommand() {
        super("info", "info", "See cloud information's.");
    }

    @Override
    public void onCommand(final String[] args) {
        Utils.printCloudInfos();
    }
}