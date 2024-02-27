package com.bedrockcloud.bedrockcloud;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoftwareManager {
    private static final Executor executor = Executors.newFixedThreadPool(10);

    public final static int SOFTWARE_PROXY = 0;
    public final static int SOFTWARE_SERVER = 1;


    //Software URLs
    public final static String POCKETMINE_URL = "https://github.com/pmmp/PocketMine-MP/releases/latest/download/PocketMine-MP.phar";
    public final static String WATERDOGPE_URL = "https://github.com/WaterdogPE/WaterdogPE/releases/latest/download/Waterdog.jar";

    //Plugin URLs
    public final static String CLOUDBRIDGEPM_URL = "https://github.com/BedrockCloud/CloudBridge-PM/releases/latest/download/CloudBridge.phar";
    public final static String DEVTOOLS_URL = "https://poggit.pmmp.io/r/208640/PocketMine-DevTools.phar";
    public final static String CLOUDBRIDGEWD_URL = "https://github.com/BedrockCloud/CloudBridge-Proxy/releases/latest/download/CloudBridge.jar";

    public static CompletableFuture<Boolean> downloadAsync(final String url, final String destinationPath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL downloadUrl = new URL(url);
                InputStream inputStream = downloadUrl.openStream();
                Path destination = Path.of(destinationPath);

                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
                return true;
            } catch (IOException e) {
                Cloud.getLogger().error("Download failed: " + e.getMessage());
                return false;
            }
        }, executor);
    }
}