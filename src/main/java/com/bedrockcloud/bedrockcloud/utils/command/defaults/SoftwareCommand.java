package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

import java.util.concurrent.CompletableFuture;

public class SoftwareCommand extends Command implements Loggable
{
    public SoftwareCommand() {
        super("software", "software <all|pocketmine|waterdogpe>", "Update Proxy/Server softwares");
    }
    
    @Override
    public void onCommand(final String[] args) {
        if (args.length != 0) {
            final String s = args[0];

            switch (s) {
                case "all" -> {
                    SoftwareManager.downloadAsync(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar").whenComplete((success, error) -> {
                        if (success) {
                            BedrockCloud.getLogger().info("PocketMine downloaded!");
                        } else {
                            BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                        }
                    });

                    SoftwareManager.downloadAsync(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar").whenComplete((success, error) -> {
                        if (success) {
                            BedrockCloud.getLogger().info("WaterdogPE downloaded!");
                        } else {
                            BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                        }
                    });
                }
                case "pocketmine" -> {
                    if (args.length != 1) {
                        SoftwareManager.downloadAsync(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar").whenComplete((success, error) -> {
                            if (success) {
                                BedrockCloud.getLogger().info("PocketMine downloaded!");
                            } else {
                                BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                            }
                        });
                    }
                }
                case "waterdogpe" -> {
                    SoftwareManager.downloadAsync(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar").whenComplete((success, error) -> {
                        if (success) {
                            BedrockCloud.getLogger().info("WaterdogPE downloaded!");
                        } else {
                            BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                        }
                    });
                }
            }
        }
    }
}
