package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerKickPacket;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerMovePacket;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

public class PlayerCommand extends Command {

    public PlayerCommand() {
        super("player", "player <list | message | kick | transfer> [player] [message | reason | server]", "Manage players");
    }

    @Override
    public void onCommand(final String[] args) {
        if (args.length == 0) {
            BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
            return;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "list" -> listPlayers();
            case "message" -> sendMessage(args);
            case "kick" -> kickPlayer(args);
            case "transfer" -> transferPlayer(args);
            default -> BedrockCloud.getLogger().warning("Try to execute: " + this.getUsage());
        }
    }

    private void listPlayers() {
        BedrockCloud.getLogger().info("§e»§r §7There are currently " + BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().size() + " Player connected! §e«");
        for (CloudPlayer cloudPlayer : BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().values()) {
            BedrockCloud.getLogger().info("§c➤ §rPlayer: " + cloudPlayer.getPlayerName() + " (" + cloudPlayer.getAddress() + ") | (" + cloudPlayer.getXuid() + ") ᐅ " + cloudPlayer.getCurrentProxy() + " / " + cloudPlayer.getCurrentServer());
        }
    }

    private void sendMessage(String[] args) {
        if (args.length != 3) {
            BedrockCloud.getLogger().warning("Try to execute: player message [player] [message]");
            return;
        }

        String playerName = args[1];
        String message = args[2];

        if (!BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            BedrockCloud.getLogger().error("This player doesn't exist.");
            return;
        }

        PlayerTextPacket playerTextPacket = new PlayerTextPacket();
        playerTextPacket.playerName = playerName;
        playerTextPacket.type = 0;
        playerTextPacket.value = message;
        BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerTextPacket);
    }

    private void kickPlayer(String[] args) {
        if (args.length != 3) {
            BedrockCloud.getLogger().warning("Try to execute: player kick [player] [reason]");
            return;
        }

        String playerName = args[1];
        String reason = args[2];

        if (!BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            BedrockCloud.getLogger().error("This player doesn't exist.");
            return;
        }

        PlayerKickPacket playerKickPacket = new PlayerKickPacket();
        playerKickPacket.playerName = playerName;
        playerKickPacket.reason = reason;
        BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerKickPacket);
    }

    private void transferPlayer(String[] args) {
        if (args.length != 3) {
            BedrockCloud.getLogger().warning("Try to execute: player transfer [player] [server]");
            return;
        }

        String playerName = args[1];
        String server = args[2];

        if (!BedrockCloud.getCloudPlayerProvider().existsPlayer(playerName)) {
            BedrockCloud.getLogger().error("This player doesn't exist.");
            return;
        }

        if (!BedrockCloud.getCloudServerProvider().existServer(server)) {
            BedrockCloud.getLogger().error("This server doesn't exist.");
            return;
        }

        PlayerMovePacket playerMovePacket = new PlayerMovePacket();
        playerMovePacket.playerName = playerName;
        playerMovePacket.toServer = server;
        BedrockCloud.getCloudPlayerProvider().getCloudPlayer(playerName).getProxy().pushPacket(playerMovePacket);
    }
}