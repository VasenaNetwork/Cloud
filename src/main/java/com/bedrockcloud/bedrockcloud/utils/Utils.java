package com.bedrockcloud.bedrockcloud.utils;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.CloudStarter;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.VersionInfo;
import com.bedrockcloud.bedrockcloud.network.DataPacket;
import com.bedrockcloud.bedrockcloud.network.packets.CloudNotifyMessagePacket;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServer;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

public class Utils {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void printCloudInfos() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        long usedMemoryBytes = Utils.getTotalMemory() - Utils.getFreeMemory();
        double usedMemoryGB = (double) usedMemoryBytes / (1024 * 1024 * 1024);

        long freeMemoryBytes = Utils.getFreeMemory();
        double freeMemoryGB = (double) freeMemoryBytes / (1024 * 1024 * 1024);

        long totalMemoryBytes = Utils.getTotalMemory();
        double totalMemoryGB = (double) totalMemoryBytes / (1024 * 1024 * 1024);

        long maxMemoryBytes = Utils.getMaxMemory();
        double maxMemoryGB = (double) maxMemoryBytes / (1024 * 1024 * 1024);

        BedrockCloud.getLogger().command("Used Memory   : " + decimalFormat.format(usedMemoryGB) + " GB");
        BedrockCloud.getLogger().command("Free Memory   : " + decimalFormat.format(freeMemoryGB) + " GB");
        BedrockCloud.getLogger().command("Total Memory  : " + decimalFormat.format(totalMemoryGB) + " GB");
        BedrockCloud.getLogger().command("Max Memory    : " + decimalFormat.format(maxMemoryGB) + " GB");
    }

    public static String getCloudPath(){
        try {
            String path = BedrockCloud.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            String fullPath = path.substring(path.lastIndexOf("/") + 1);
            return path.replace(fullPath, "");
        } catch (NullPointerException | URISyntaxException e){
            BedrockCloud.getLogger().exception(e);
            return "";
        }
    }

    public static Config getTemplateConfig() {
        return new Config("./templates/config.json", Config.JSON);
    }

    public static Config getConfig() {
        return new Config("./local/config.yml", Config.YAML);
    }

    public static VersionInfo getVersion() {
        return CloudStarter.class.isAnnotationPresent(VersionInfo.class) ? CloudStarter.class.getAnnotation(VersionInfo.class) : null;
    }

    public static String boolToString(Boolean bool){
        return (bool ? "§aYes" : "§cNo");
    }

    public static String getServiceSeperator(){
        return getConfig().getString("service-separator", "-");
    }

    public static void writeFile(File file, InputStream content) throws IOException {
        if ( content == null ) {
            throw new IllegalArgumentException( "Content must not be null!" );
        }

        if ( !file.exists() ) {
            file.createNewFile();
        }

        FileOutputStream stream = new FileOutputStream( file );

        byte[] buffer = new byte[1024];
        int length;
        while ( ( length = content.read( buffer ) ) != -1 ) {
            stream.write( buffer, 0, length );
        }
        content.close();
        stream.close();
    }

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

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getUsedMemory() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return heapMemoryUsage.getUsed();
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static void broadcastPacket(final DataPacket packet) {
        for (CloudServer server : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            if (server.getTemplate().getType() == SoftwareManager.SOFTWARE_SERVER) {
                if (server.isConnected()) server.pushPacket(packet);
            }
        }
    }

    @ApiStatus.Internal
    public static void sendNotifyCloud(final String message) {
        for (final CloudServer cloudServer : BedrockCloud.getCloudServerProvider().getCloudServers().values()) {
            if (cloudServer.getTemplate().getType() == SoftwareManager.SOFTWARE_PROXY) {
                final CloudNotifyMessagePacket packet = new CloudNotifyMessagePacket();
                packet.message = BedrockCloud.prefix + message;
                cloudServer.pushPacket(packet);
            }
        }
    }
}