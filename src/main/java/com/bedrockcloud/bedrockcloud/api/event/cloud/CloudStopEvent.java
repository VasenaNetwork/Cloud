package com.bedrockcloud.bedrockcloud.api.event.cloud;

import com.bedrockcloud.bedrockcloud.BedrockCloud;

public class CloudStopEvent extends CloudEvent {
    public CloudStopEvent(BedrockCloud cloud) {
        super(cloud);
    }
}
