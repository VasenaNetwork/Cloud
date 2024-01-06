package com.bedrockcloud.bedrockcloud.utils.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;

public class MaintenanceManager {

    public static void addMaintenance(String player){
        BedrockCloud.getMaintenanceFile().set(player.toLowerCase(), true);
        BedrockCloud.getMaintenanceFile().save();
    }

    public static void removeMaintenance(String player){
        BedrockCloud.getMaintenanceFile().remove(player.toLowerCase());
        BedrockCloud.getMaintenanceFile().save();
    }

    public static boolean isMaintenance(String player){
        return BedrockCloud.getMaintenanceFile().exists(player.toLowerCase(), true);
    }
}
