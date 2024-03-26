package com.bedrockcloud.bedrockcloud.templates;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.api.event.template.TemplateUnloadEvent;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.files.json.JsonUtils;
import org.jetbrains.annotations.ApiStatus;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class TemplateProvider implements Loggable {
    private final HashMap<String, Template> templateMap;
    private final HashMap<String, Template> runningTemplates;

    public TemplateProvider() {
        this.templateMap = new HashMap<>();
        this.runningTemplates = new HashMap<>();
    }

    public HashMap<String, Template> getRunningTemplates() {
        return this.runningTemplates;
    }

    public HashMap<String, Template> getTemplateMap() {
        return this.templateMap;
    }

    @ApiStatus.Internal
    public void addTemplate(final Template template) {
        this.templateMap.put(template.getName(), template);
    }

    @ApiStatus.Internal
    public Template getTemplate(final String name) {
        return this.templateMap.get(name);
    }

    public boolean existsTemplate(final String name) {
        return this.templateMap.containsKey(name);
    }

    @ApiStatus.Internal
    public void removeTemplate(final String name) {
        this.templateMap.remove(name);
    }

    @ApiStatus.Internal
    public void removeTemplate(final Template template) {
        this.templateMap.remove(template.getName());
    }

    public boolean isTemplateRunning(final Template template) {
        return this.runningTemplates.containsKey(template.getName());
    }

    @ApiStatus.Internal
    public void addRunningTemplate(final Template template) {
        this.runningTemplates.put(template.getName(), template);
    }

    @ApiStatus.Internal
    public void removeRunningTemplate(final String name) {
        TemplateUnloadEvent event = new TemplateUnloadEvent(this.getTemplate(name));
        Cloud.getInstance().getPluginManager().callEvent(event);

        this.runningTemplates.remove(name);
    }

    @ApiStatus.Internal
    public void removeRunningTemplate(final Template template) {
        TemplateUnloadEvent event = new TemplateUnloadEvent(template);
        Cloud.getInstance().getPluginManager().callEvent(event);

        this.runningTemplates.remove(template.getName());
    }

    public void loadTemplate(String name) {
        if (!existsTemplate(name)) {
            try {
                JSONObject stats = JsonUtils.readJsonFromFile("./templates/config.json");
                if (stats != null && !stats.isEmpty()) {
                    new Template(name, (int) stats.get("minRunningServer"), (int) stats.get("maxRunningServer"),
                            (int) stats.get("maxPlayer"), (int) stats.get("type"), (boolean) stats.get("beta"),
                            (boolean) stats.get("maintenance"), (boolean) stats.get("isLobby"),
                            (boolean) stats.get("isStatic"));
                }
            } catch (IOException e) {
                Cloud.getLogger().exception(e);
            }
        } else {
            Cloud.getLogger().error("The template " + name + " is already loaded.");
        }
    }

    @ApiStatus.Internal
    public void loadTemplates() {
        for (String name : GroupAPI.getGroups()) {
            if (!existsTemplate(name)) {
                try {
                    JSONObject stats = JsonUtils.readJsonFromFile("./templates/config.json");
                    if (stats != null && !stats.isEmpty()) {
                        new Template(name, (int) stats.get("minRunningServer"), (int) stats.get("maxRunningServer"),
                                (int) stats.get("maxPlayer"), (int) stats.get("type"), (boolean) stats.get("beta"),
                                (boolean) stats.get("maintenance"), (boolean) stats.get("isLobby"),
                                (boolean) stats.get("isStatic"));
                    }
                } catch (IOException e) {
                    Cloud.getLogger().exception(e);
                }
            }
        }
    }
}
