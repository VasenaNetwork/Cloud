package com.bedrockcloud.bedrockcloud.api.event.template;

import com.bedrockcloud.bedrockcloud.templates.Template;

public class TemplateLoadEvent extends TemplateEvent {
    public TemplateLoadEvent(Template template) {
        super(template);
    }
}
