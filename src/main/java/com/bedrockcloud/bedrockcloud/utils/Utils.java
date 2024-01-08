package com.bedrockcloud.bedrockcloud.utils;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.CloudStarter;
import com.bedrockcloud.bedrockcloud.VersionInfo;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.manager.MemoryManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        long usedMemoryBytes = MemoryManager.getTotalMemory() - MemoryManager.getFreeMemory();
        double usedMemoryGB = (double) usedMemoryBytes / (1024 * 1024 * 1024);

        long freeMemoryBytes = MemoryManager.getFreeMemory();
        double freeMemoryGB = (double) freeMemoryBytes / (1024 * 1024 * 1024);

        long totalMemoryBytes = MemoryManager.getTotalMemory();
        double totalMemoryGB = (double) totalMemoryBytes / (1024 * 1024 * 1024);

        long maxMemoryBytes = MemoryManager.getMaxMemory();
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
}