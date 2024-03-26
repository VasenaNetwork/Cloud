package com.bedrockcloud.bedrockcloud.api.event.template;

import com.bedrockcloud.bedrockcloud.templates.Template;

public class TemplateCreateEvent extends TemplateEvent {

    public TemplateCreateEvent(Template template) {
        super(template);
    }
}
