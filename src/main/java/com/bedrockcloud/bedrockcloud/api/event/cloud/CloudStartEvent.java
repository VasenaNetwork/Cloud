package com.bedrockcloud.bedrockcloud.api.event.cloud;

import com.bedrockcloud.bedrockcloud.Cloud;

public class CloudStartEvent extends CloudEvent {
    public CloudStartEvent(Cloud cloud) {
        super(cloud);
    }
}
