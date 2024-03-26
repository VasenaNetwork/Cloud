package com.bedrockcloud.bedrockcloud.utils.files;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.SoftwareManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

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
                    if (!dir.mkdirs()) {
                        getLogger().error("Failed to create directory " + directory + "!");
                    }
                }
            }

            createConfigFiles();
            downloadMissingFiles();
            createDefaultGroups();
            getLogger().info("§aStarting cloud...");
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createConfigFiles() throws IOException {
        File localConfigFile = new File("./local/config.yml");

        if (!localConfigFile.exists()) {
            localConfigFile.getParentFile().mkdirs();
            localConfigFile.createNewFile();

            Properties config = new Properties();
            try (FileReader reader = new FileReader(localConfigFile)) {
                config.load(reader);
            } catch (Exception e) {
                getLogger().exception(e);
            }

            try (FileWriter writer = new FileWriter(localConfigFile)) {
                String[] configKeys = {
                        "port: " + (config.getProperty("port") != null ? Double.toString(Double.parseDouble(config.getProperty("port"))) : Double.toString((double) this.cloudPort)),
                        "debug-mode: " + (config.getProperty("debug-mode") != null ? config.getProperty("debug-mode") : "false"),
                        "motd: \"" + (config.getProperty("motd") != null ? config.getProperty("motd") : "Default Cloud Service") + "\"",
                        "auto-update-on-start: " + (config.getProperty("auto-update-on-start") != null ? config.getProperty("auto-update-on-start") : false),
                        "wdpe-login-extras: " + (config.getProperty("wdpe-login-extras") != null ? config.getProperty("wdpe-login-extras") : false),
                        "enable-log: " + (config.getProperty("enable-log") != null ? config.getProperty("enable-log") : "true"),
                        "start-method: \"" + (config.getProperty("start-method") != null ? config.getProperty("start-method") : "tmux") + "\"",
                        "rest-password: \"" + (config.getProperty("rest-password") != null ? config.getProperty("rest-password") : Utils.generateRandomPassword(8)) + "\"",
                        "rest-port: \"" + (config.getProperty("rest-port") != null ? Double.toString(Double.parseDouble(config.getProperty("rest-port"))) : "8080.0") + "\"",
                        "rest-username: \"" + (config.getProperty("rest-username") != null ? config.getProperty("rest-username") : "cloud") + "\"",
                        "rest-enabled: \"" + (config.getProperty("rest-enabled") != null ? config.getProperty("rest-enabled") : "true") + "\"",
                        "service-separator: \"" + (config.getProperty("service-separator") != null ? config.getProperty("service-separator") : "-") + "\""
                };

                for (String keyValue : configKeys) {
                    String[] parts = keyValue.split(": ", 2);
                    String key = parts[0];
                    String value = parts[1];
                    if (!config.containsKey(key)) {
                        writer.write(keyValue + "\n");
                    }
                }

                writer.flush();
            } catch (IOException e) {
                getLogger().error("Error writing to config file: " + e.getMessage());
            } catch (Exception e) {
                getLogger().exception(e);
            }
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