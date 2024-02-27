package com.bedrockcloud.bedrockcloud.api.event.cloud;

import com.bedrockcloud.bedrockcloud.Cloud;

public class CloudStopEvent extends CloudEvent {
    public CloudStopEvent(Cloud cloud) {
        super(cloud);
    }
}
