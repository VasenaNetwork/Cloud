package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import java.util.Objects;

import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

public class TemplateCommand extends Command {
    public TemplateCommand() {
        super("template", "template <start | restart | stop | create | list | delete>", "Manage templates");
    }

    @Override
    public void onCommand(final String[] args) {
        if (args.length > 0) {
            if (Objects.equals(args[0], "list")) {
                this.getLogger().info("§e»§r §7There are currently " + BedrockCloud.getTemplateProvider().getTemplateMap().size() + " Templates online! §e«");
                for (final Template template : BedrockCloud.getTemplateProvider().getTemplateMap().values()) {
                    if (template.getType() == 0) {
                        this.getLogger().info("§c➤ §rName: " + template.getName() + " | Maintenance: " + template.getMaintenance() + " | Beta: " + template.getBeta() + " | TYPE: WATERDOGPE");
                    }
                }
                for (final Template template : BedrockCloud.getTemplateProvider().getTemplateMap().values()) {
                    if (template.getType() == 1) {
                        this.getLogger().info("§c➤ §rName: " + template.getName() + " | Maintenance: " + template.getMaintenance() + " | Beta: " + template.getBeta() + " | TYPE: POCKETMINE");
                    }
                }
            }

            if (args.length != 1) {
                final String subcommand = args[0];
                if (subcommand.equalsIgnoreCase("start")) {
                    if (args.length == 2) {
                        final String templateName = args[1];
                        final Template template = BedrockCloud.getTemplateProvider().getTemplate(templateName);
                        if (template == null) {
                            BedrockCloud.getLogger().error("This Template doesn't exist");
                            return;
                        }
                        if (BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
                            BedrockCloud.getLogger().error("The Template is already running!");
                            return;
                        }
                        template.start(true);
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: template <start> [TemplateName]");
                    }
                } else if (subcommand.equalsIgnoreCase("stop")) {
                    if (args.length == 2) {
                        final String templateName = args[1];
                        final Template template2 = BedrockCloud.getTemplateProvider().getTemplate(templateName);
                        if (template2 == null) {
                            BedrockCloud.getLogger().error("This Template doesn't exist");
                            return;
                        }
                        if (!BedrockCloud.getTemplateProvider().isTemplateRunning(template2)) {
                            BedrockCloud.getLogger().error("The Template is not running!");
                            return;
                        }
                        template2.stop();
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: template <stop> [TemplateName]");
                    }
                } else if (subcommand.equalsIgnoreCase("restart")) {
                    if (args.length == 2) {
                        final String templateName = args[1];
                        final Template template2 = BedrockCloud.getTemplateProvider().getTemplate(templateName);
                        if (template2 == null) {
                            BedrockCloud.getLogger().error("This Template doesn't exist");
                            return;
                        }
                        if (!BedrockCloud.getTemplateProvider().isTemplateRunning(template2)) {
                            BedrockCloud.getLogger().error("The Template is not running!");
                            return;
                        }
                        template2.restart();
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: template <restart> [TemplateName]");
                    }
                } else if (subcommand.equalsIgnoreCase("delete")) {
                    if (args.length == 2){
                        if (args[1] != null) {
                            if (GroupAPI.deleteGroup(args[1])){
                                BedrockCloud.getLogger().info("§aThe group §e" + args[1] + " §awas deleted successfully§7.");
                            }
                        } else {
                            BedrockCloud.getLogger().warning("Try to execute: template <delete> [name]");
                        }
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: template <delete> [name]");
                    }
                } else if (subcommand.equalsIgnoreCase("create")) {
                    if (args.length == 3) {
                        if (args[2] != null) {
                            switch (args[2]) {
                                case "waterdogpe" -> GroupAPI.createGroup(args[1], 0);
                                case "pocketmine" -> GroupAPI.createGroup(args[1], 1);
                                default ->
                                        BedrockCloud.getLogger().warning("Try to execute: template <create> [pocketmine | waterdogpe]");
                            }
                        } else {
                            BedrockCloud.getLogger().warning("Try to execute: template <create> [pocketmine | waterdogpe]");
                        }
                    } else {
                        BedrockCloud.getLogger().warning("Try to execute: template <create> [pocketmine | waterdogpe]");
                    }
                } else {
                    BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
                }
            }
        } else {
            BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
        }
    }
}
