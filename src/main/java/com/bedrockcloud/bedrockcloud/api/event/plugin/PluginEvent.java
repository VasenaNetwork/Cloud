package com.bedrockcloud.bedrockcloud.api.event.plugin;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.api.plugin.Plugin;

public class PluginEvent extends Event {

    private final Plugin plugin;

    public PluginEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
