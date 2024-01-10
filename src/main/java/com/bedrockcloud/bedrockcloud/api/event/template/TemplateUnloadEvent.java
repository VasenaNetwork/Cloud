package com.bedrockcloud.bedrockcloud.api.event.template;

import com.bedrockcloud.bedrockcloud.templates.Template;

public class TemplateUnloadEvent extends TemplateEvent {
    public TemplateUnloadEvent(Template template) {
        super(template);
    }
}
