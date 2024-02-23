package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

public class TemplateCommand extends Command {

    public TemplateCommand() {
        super("template", "template <start | restart | stop | create | list | delete>", "Manage templates");
    }

    @Override
    public void onCommand(final String[] args) {
        if (args.length == 0) {
            BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
            return;
        }

        String subcommand = args[0];

        switch (subcommand.toLowerCase()) {
            case "list" -> listTemplates();
            case "start" -> executeStartCommand(args);
            case "stop" -> executeStopCommand(args);
            case "restart" -> executeRestartCommand(args);
            case "create" -> executeCreateCommand(args);
            case "delete" -> executeDeleteCommand(args);
            default -> BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
        }
    }

    private void listTemplates() {
        BedrockCloud.getLogger().info("§e»§r §7There are currently " + BedrockCloud.getTemplateProvider().getTemplateMap().size() + " Templates online! §e«");
        for (Template template : BedrockCloud.getTemplateProvider().getTemplateMap().values()) {
            String type = template.getType() == 0 ? "WATERDOGPE" : "POCKETMINE";
            BedrockCloud.getLogger().info("§c➤ §rName: " + template.getName() + " | Maintenance: " + template.isMaintenance() + " | Beta: " + template.isBeta() + " | TYPE: " + type);
        }
    }

    private void executeStartCommand(String[] args) {
        if (args.length == 2) {
            String templateName = args[1];
            Template template = BedrockCloud.getTemplateProvider().getTemplate(templateName);
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
    }

    private void executeStopCommand(String[] args) {
        if (args.length == 2) {
            String templateName = args[1];
            Template template = BedrockCloud.getTemplateProvider().getTemplate(templateName);
            if (template == null) {
                BedrockCloud.getLogger().error("This Template doesn't exist");
                return;
            }
            if (!BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
                BedrockCloud.getLogger().error("The Template is not running!");
                return;
            }
            template.stop();
        } else {
            BedrockCloud.getLogger().warning("Try to execute: template <stop> [TemplateName]");
        }
    }

    private void executeRestartCommand(String[] args) {
        if (args.length == 2) {
            String templateName = args[1];
            Template template = BedrockCloud.getTemplateProvider().getTemplate(templateName);
            if (template == null) {
                BedrockCloud.getLogger().error("This Template doesn't exist");
                return;
            }
            if (!BedrockCloud.getTemplateProvider().isTemplateRunning(template)) {
                BedrockCloud.getLogger().error("The Template is not running!");
                return;
            }
            template.restart();
        } else {
            BedrockCloud.getLogger().warning("Try to execute: template <restart> [TemplateName]");
        }
    }

    private void executeCreateCommand(String[] args) {
        if (args.length == 3) {
            String name = args[1];
            String type = args[2].toLowerCase();
            switch (type) {
                case "waterdogpe" -> GroupAPI.createNewGroup(name, 0, false);
                case "pocketmine" -> GroupAPI.createNewGroup(name, 1, false);
                default ->
                        BedrockCloud.getLogger().warning("Try to execute: template <create> <name> [pocketmine | waterdogpe]");
            }
        } else {
            BedrockCloud.getLogger().warning("Try to execute: template <create> <name> [pocketmine | waterdogpe]");
        }
    }

    private void executeDeleteCommand(String[] args) {
        if (args.length == 2 && args[1] != null) {
            if (GroupAPI.deleteGroup(args[1])) {
                BedrockCloud.getLogger().info("§aThe group §e" + args[1] + " §awas deleted successfully§7.");
            }
        } else {
            BedrockCloud.getLogger().warning("Try to execute: template <delete> [name]");
        }
    }
}