package com.bedrockcloud.bedrockcloud.api.event.template;

import com.bedrockcloud.bedrockcloud.api.event.Cancellable;
import com.bedrockcloud.bedrockcloud.templates.Template;

public class TemplateLoadEvent extends TemplateEvent implements Cancellable {
    public TemplateLoadEvent(Template template) {
        super(template);
    }
}
