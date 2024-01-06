package com.bedrockcloud.bedrockcloud.utils.console;

public interface Loggable
{
    default Logger getLogger() {
        return new Logger();
    }
}
