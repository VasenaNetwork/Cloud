package com.bedrockcloud.bedrockcloud.templates;

import java.io.File;
import java.io.IOException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.event.template.TemplateEvent;
import com.bedrockcloud.bedrockcloud.api.event.template.TemplateLoadEvent;
import com.bedrockcloud.bedrockcloud.api.event.template.TemplateUnloadEvent;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.files.json.json;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;

public class TemplateProvider implements Loggable
{
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
        return this.templateMap.get(name) != null;
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
    public void removeRunningGroup(final String name) {
        TemplateUnloadEvent event = new TemplateUnloadEvent(BedrockCloud.getTemplateProvider().getTemplate(name));
        BedrockCloud.getInstance().getPluginManager().callEvent(event);

        this.runningTemplates.remove(name);
    }

    @ApiStatus.Internal
    public void removeRunningGroup(final Template group) {
        TemplateUnloadEvent event = new TemplateUnloadEvent(group);
        BedrockCloud.getInstance().getPluginManager().callEvent(event);

        this.runningTemplates.remove(group.getName());
    }

    public void loadTemplate(String name) {
        final HashMap<String, Object> stats;
        try {
            if (!BedrockCloud.getTemplateProvider().existsTemplate(name)) {
                stats = (HashMap<String, Object>) json.get(name, json.ALL);
                if (stats != null && !stats.isEmpty()) {
                    new Template(name, Math.toIntExact((Long) stats.get("minRunningServer")), Math.toIntExact((Long) stats.get("maxRunningServer")), Math.toIntExact((Long) stats.get("maxPlayer")), Math.toIntExact((Long) stats.get("type")), (Boolean) stats.get("beta"), (Boolean) stats.get("maintenance"), (Boolean) stats.get("isLobby"), (Boolean) stats.get("isStatic"));
                }
            } else {
                BedrockCloud.getLogger().error("§cThe template §e" + name + " §cis already loaded.");
            }
        } catch (IOException e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    @ApiStatus.Internal
    public void loadTemplates() {
        for (final String name : GroupAPI.getGroups()) {
            try {
                final HashMap<String, Object> stats = (HashMap<String, Object>) json.get(name, json.ALL);
                if (stats != null && !stats.isEmpty()) {
                    new Template(name, Math.toIntExact((Long) stats.get("minRunningServer")), Math.toIntExact((Long) stats.get("maxRunningServer")), Math.toIntExact((Long) stats.get("maxPlayer")), Math.toIntExact((Long) stats.get("type")), (Boolean) stats.get("beta"), (Boolean) stats.get("maintenance"), (Boolean) stats.get("isLobby"), (Boolean) stats.get("isStatic"));
                }
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }

        File directory = new File("./temp/");
        if (directory.exists() && directory.isDirectory()) {
            File[] folders = directory.listFiles(File::isDirectory);

            if (folders != null && folders.length >= 1) {
                for (File folder : folders) {
                    for (Template template : this.getTemplateMap().values()) {
                        if (template != null) {
                            if (folder.getName().startsWith(template.getName())) {
                                if (!template.getStatic()) {
                                    folder.delete();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}