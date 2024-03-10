package com.bedrockcloud.bedrockcloud.utils.files;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.SoftwareManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Startfiles implements Loggable {
    private final ArrayList<String> directories;
    private final int cloudPort;

    public Startfiles(int cloudPort) {
        this.cloudPort = cloudPort;
        this.directories = new ArrayList<>();
        initializeDirectories();
        checkFolders();
    }

    private void initializeDirectories() {
        this.directories.add("./templates");
        this.directories.add("./temp");
        this.directories.add("./local");
        this.directories.add("./archive");
        this.directories.add(this.directories.get(2) + "/plugins");
        this.directories.add(this.directories.get(2) + "/plugins/cloud");
        this.directories.add(this.directories.get(2) + "/plugins/pocketmine");
        this.directories.add(this.directories.get(2) + "/plugins/waterdogpe");
        this.directories.add(this.directories.get(2) + "/versions");
        this.directories.add(this.directories.get(2) + "/versions/pocketmine");
        this.directories.add(this.directories.get(2) + "/versions/waterdogpe");
        this.directories.add(this.directories.get(2) + "/notify");
        this.directories.add(this.directories.get(3) + "/crashdumps");
        this.directories.add(this.directories.get(3) + "/processes");
    }

    private void checkFolders() {
        try {
            for (String directory : this.directories) {
                File dir = new File(directory);
                if (!dir.exists()) {
                    getLogger().debug("Creating directory " + directory + "!");
                    dir.mkdirs();
                }
            }
            createConfigFiles();
            downloadMissingFiles();
            createDefaultGroups();
            getLogger().info("§aStarting cloud...");
            Thread.sleep(3000);
        } catch (Exception e) {
            getLogger().exception(e);
        }
    }

    private void createConfigFiles() throws IOException {
        File localConfigFile = new File("./local/config.yml");
        if (!localConfigFile.exists()) {
            Config config = new Config(localConfigFile, Config.YAML);
            config.set("port", (double) this.cloudPort);
            config.set("debug-mode", false);
            config.set("motd", "Default BedrockCloud Service");
            config.set("auto-update-on-start", false);
            config.set("wdpe-login-extras", false);
            config.set("enable-cloudlog-file", false);
            config.set("use-proxy", true);
            config.set("auto-restart-cloud", false);
            config.set("rest-password", Utils.generateRandomPassword(8));
            config.set("rest-port", 8080.0);
            config.set("rest-username", "cloud");
            config.set("rest-enabled", true);
            config.set("service-separator", "-");
            config.save();
        }

        File maintenanceFile = new File("./local/maintenance.txt");
        if (!maintenanceFile.exists()) {
            maintenanceFile.createNewFile();
        }
    }

    private void downloadMissingFiles() {
        downloadFile(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar");
        downloadFile(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar");
        downloadFile(SoftwareManager.CLOUDBRIDGEPM_URL, "./local/plugins/pocketmine/CloudBridge-PM.phar");
        downloadFile(SoftwareManager.DEVTOOLS_URL, "./local/plugins/pocketmine/DevTools.phar");
        downloadFile(SoftwareManager.CLOUDBRIDGEWD_URL, "./local/plugins/waterdogpe/CloudBridge-WD.jar");
    }

    private void downloadFile(String url, String destination) {
        File file = new File(destination);
        if (!file.exists()) {
            SoftwareManager.downloadAsync(url, destination).whenComplete((success, error) -> {
                if (success) {
                    Cloud.getLogger().info(file.getName() + " downloaded!");
                } else {
                    Cloud.getLogger().error("Download failed: " + error.getMessage());
                }
            });
        }
    }

    private void createDefaultGroups() {
        GroupAPI.createGroup("Proxy-Master", SoftwareManager.SOFTWARE_PROXY, false);
        GroupAPI.createGroup("Lobby", SoftwareManager.SOFTWARE_SERVER, true);
    }
}