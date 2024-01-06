package com.bedrockcloud.bedrockcloud.utils.command;

import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import lombok.Getter;

public class Command implements Loggable
{
    @Getter
    public String command;
    @Getter
    public String usage;
    @Getter
    public String description;
    
    public Command(final String cmd, String usage, String description) {
        this.command = cmd;
        this.usage = usage;
        this.description = description;
    }
    
    public void onCommand(final String[] args) {
    }
}
