package com.bedrockcloud.bedrockcloud.api.event.cloud;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.event.Event;

public class CloudEvent extends Event {

    private final BedrockCloud cloud;

    public CloudEvent(BedrockCloud cloud ) {
        this.cloud = cloud;
    }

    public BedrockCloud getCloud() {
        return this.cloud;
    }
}
