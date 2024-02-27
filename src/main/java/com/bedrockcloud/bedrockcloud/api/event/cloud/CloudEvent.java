package com.bedrockcloud.bedrockcloud.api.event.cloud;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.event.Event;

public class CloudEvent extends Event {

    private final Cloud cloud;

    public CloudEvent(Cloud cloud ) {
        this.cloud = cloud;
    }

    public Cloud getCloud() {
        return this.cloud;
    }
}
