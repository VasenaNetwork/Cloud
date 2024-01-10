package com.bedrockcloud.bedrockcloud.api.event.template;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.api.event.Event;
import com.bedrockcloud.bedrockcloud.templates.Template;
import lombok.Getter;

public class TemplateEvent extends Event {

    @Getter
    private final Template template;

    public TemplateEvent(Template template) {
        this.template = template;
    }
}
