package com.bedrockcloud.bedrockcloud.api.event.cloud;

import com.bedrockcloud.bedrockcloud.BedrockCloud;

public class CloudStartEvent extends CloudEvent {
    public CloudStartEvent(BedrockCloud cloud) {
        super(cloud);
    }
}
