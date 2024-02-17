package com.bedrockcloud.bedrockcloud.utils.files;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.PasswordAPI;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.bedrockcloud.bedrockcloud.api.GroupAPI;
import com.bedrockcloud.bedrockcloud.SoftwareManager;

import java.io.File;
import java.util.ArrayList;

public class Startfiles implements Loggable
{
    private final ArrayList<String> directorys;
    private final int cloudPort;

    public Startfiles(int cloudPort) {
        this.cloudPort = cloudPort;

        this.directorys = new ArrayList<>();
        try {
            this.delete(new File("./temp"));
            this.directorys.add("./templates");
            this.directorys.add("./temp");
            this.directorys.add("./local");
            this.directorys.add("./archive");
            this.directorys.add(this.directorys.get(2) + "/plugins");
            this.directorys.add(this.directorys.get(2) + "/plugins/cloud");
            this.directorys.add(this.directorys.get(2) + "/plugins/pocketmine");
            this.directorys.add(this.directorys.get(2) + "/plugins/waterdogpe");
            this.directorys.add(this.directorys.get(2) + "/versions");
            this.directorys.add(this.directorys.get(2) + "/versions/pocketmine");
            this.directorys.add(this.directorys.get(2) + "/versions/waterdogpe");
            this.directorys.add(this.directorys.get(2) + "/notify");
            this.directorys.add(this.directorys.get(3) + "/crashdumps");
            this.directorys.add(this.directorys.get(3) + "/server-pids");
        } catch (NullPointerException e) {
            this.getLogger().exception(e);
        }
        this.checkFolder();
    }

    private void delete(final File file) {
        if (file.isDirectory()) {
            final String[] fileList = file.list();
            if (fileList.length == 0) {
                file.delete();
            } else {
                for (final String fileName : fileList) {
                    final String fullPath = file.getPath() + "/" + fileName;
                    final File fileOrFolder = new File(fullPath);
                    this.delete(fileOrFolder);
                }
                this.delete(file);
            }
        } else {
            file.delete();
        }
    }

    private void checkFolder() {
        try {
            for (final String direc : this.directorys) {
                final File theDir = new File(direc);
                if (!theDir.exists()) {
                    this.getLogger().debug("Creating Folder in the Directory " + direc + "!");
                    theDir.mkdirs();
                }
            }
            final File templatesc = new File("./templates/config.json");
            if (!templatesc.exists()) {
                templatesc.createNewFile();
            }

            final File file = new File("./local/config.yml");

            if (!file.exists()) {
                Config config = new Config(file, Config.YAML);
                config.set("port", (double)this.cloudPort);
                config.set("debug-mode", false);
                config.set("motd", "Default BedrockCloud Service");
                config.set("auto-update-on-start", false);
                config.set("wdpe-login-extras", false);
                config.set("enable-cloudlog-file", false);
                config.set("use-proxy", true);
                config.set("auto-restart-cloud", false);
                config.set("rest-password", PasswordAPI.generateRandomPassword(8));
                config.set("rest-port", 8080.0);
                config.set("rest-username", "cloud");
                config.set("rest-enabled", true);
                config.set("service-separator", "-");
                config.save(file);
            }

            final File maintenance = new File("./local/maintenance.txt");
            if (!maintenance.exists()){
                Config config = new Config(file, Config.ENUM);
                config.save();
            }

            final File pocketmineFile = new File("./local/versions/pocketmine/PocketMine-MP.phar");
            final File waterdogFile = new File("./local/versions/waterdogpe/WaterdogPE.jar");

            if (!pocketmineFile.exists()) {
                SoftwareManager.downloadAsync(SoftwareManager.POCKETMINE_URL, "./local/versions/pocketmine/PocketMine-MP.phar").whenComplete((success, error) -> {
                    if (success) {
                        BedrockCloud.getLogger().info("WaterdogPE downloaded!");
                    } else {
                        BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                    }
                });
            }

            if (!waterdogFile.exists()) {
                SoftwareManager.downloadAsync(SoftwareManager.WATERDOGPE_URL, "./local/versions/waterdogpe/WaterdogPE.jar").whenComplete((success, error) -> {
                    if (success) {
                        BedrockCloud.getLogger().info("WaterdogPE downloaded!");
                    } else {
                        BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                    }
                });
            }

            final File cloudbridgepmFile = new File("./local/plugins/pocketmine/CloudBridge-PM.phar");
            final File devtools = new File("./local/plugins/pocketmine/DevTools.phar");
            final File cloudbridgewdFile = new File("./local/plugins/waterdogpe/CloudBridge-WD.jar");

            if (!cloudbridgepmFile.exists()) {
                SoftwareManager.downloadAsync(SoftwareManager.CLOUDBRIDGEPM_URL, "./local/plugins/pocketmine/CloudBridge-PM.phar").whenComplete((success, error) -> {
                    if (success) {
                        BedrockCloud.getLogger().info("CloudBridge-PM downloaded!");
                    } else {
                        BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                    }
                });
            }

            if (!devtools.exists()) {
                SoftwareManager.downloadAsync(SoftwareManager.DEVTOOLS_URL, "./local/plugins/pocketmine/DevTools.phar").whenComplete((success, error) -> {
                    if (success) {
                        BedrockCloud.getLogger().info("DevTools downloaded!");
                    } else {
                        BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                    }
                });
            }

            if (!cloudbridgewdFile.exists()) {
                SoftwareManager.downloadAsync(SoftwareManager.CLOUDBRIDGEWD_URL, "./local/plugins/waterdogpe/CloudBridge-WD.jar").whenComplete((success, error) -> {
                    if (success) {
                        BedrockCloud.getLogger().info("CloudBridge-WD downloaded!");
                    } else {
                        BedrockCloud.getLogger().error("Download failed: " + error.getMessage());
                    }
                });
            }
            GroupAPI.createGroup("Proxy-Master", SoftwareManager.SOFTWARE_PROXY);
            GroupAPI.createGroup("Lobby", SoftwareManager.SOFTWARE_SERVER);

            BedrockCloud.getLogger().info("§aStarting cloud...");
            Thread.sleep(3000);
        } catch (Exception e) {
            this.getLogger().exception(e);
        }
    }
}