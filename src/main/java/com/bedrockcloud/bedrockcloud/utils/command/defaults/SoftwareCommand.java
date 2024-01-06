package com.bedrockcloud.bedrockcloud.utils.command.defaults;

import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.utils.command.Command;

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
                    if (SoftwareManager.download(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar")) {
                        this.getLogger().info("PocketMine downloaded");
                    } else {
                        this.getLogger().error("Download server offline!");
                    }
                    if (SoftwareManager.download(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar")) {
                        this.getLogger().info("WaterdogPE downloaded");
                    } else {
                        this.getLogger().error("Download server offline!");
                    }
                }
                case "pocketmine" -> {
                    if (args.length != 1) {
                        if (SoftwareManager.download(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar")) {
                            this.getLogger().info("PocketMine downloaded");
                        } else {
                            this.getLogger().error("Download server offline!");
                        }
                    }
                }
                case "waterdogpe" -> {
                    if (SoftwareManager.download(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar")) {
                        this.getLogger().info("WaterdogPE downloaded");
                    } else {
                        this.getLogger().error("Download server offline!");
                    }
                }
            }
        }
    }
}
