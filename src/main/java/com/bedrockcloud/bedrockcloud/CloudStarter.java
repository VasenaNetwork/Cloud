package com.bedrockcloud.bedrockcloud;

import com.bedrockcloud.bedrockcloud.utils.files.Startfiles;
import com.bedrockcloud.bedrockcloud.port.PortValidator;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@VersionInfo(name = "BedrockCloud", version = "1.0.2", developers = { "xxFLORII" }, identifier = "@Beta")
public class CloudStarter {

    @Getter
    private static String cloudUser;

    public static void main(String[] args) {
        try {
            Class.forName("com.bedrockcloud.bedrockcloud.BedrockCloud");
            cloudUser = System.getProperty("user.name");

            int javaVersion = getJavaVersion();
            if (javaVersion < 17) {
                BedrockCloud.getLogger().error("Using unsupported Java version! Minimum supported version is Java 17, found Java " + javaVersion);
                Runtime.getRuntime().halt(0);
                return;
            }

            if (!isLinux()){
                BedrockCloud.getLogger().error("You need a Linux distribution to use BedrockCloud.");
                Runtime.getRuntime().halt(0);
                return;
            }

            File file = new File("./bin");
            if (!file.exists()){
                BedrockCloud.getLogger().error("No PocketMine PHP binary was found. This is needed to start the PocketMine servers.");
                Runtime.getRuntime().halt(0);
                return;
            }

            new Startfiles(PortValidator.getFreeCloudPort());

            Pattern pattern = Pattern.compile("^[^a-zA-Z0-9]+$");
            Matcher matcher = pattern.matcher(Utils.getServiceSeperator());
            if (Utils.getServiceSeperator().isEmpty() || Utils.getServiceSeperator().length() != 1 || !matcher.matches()){
                BedrockCloud.getLogger().error("Service separator is invalid. Please check your cloud configuration.");
                Runtime.getRuntime().halt(0);
                return;
            }

            Thread.currentThread().setName("BedrockCloud-main");

            try {
                if (!Objects.equals(getVersion(), Objects.requireNonNull(Utils.getVersion()).version())) {
                    BedrockCloud.getLogger().info("§cYou are not using the latest stable version of BedrockCloud.");
                    BedrockCloud.getLogger().info("§cLatest stable version§f: §e" + getVersion());
                    BedrockCloud.getLogger().info("§cCurrent version§f: §e" + Objects.requireNonNull(Utils.getVersion()).version());
                } else {
                    BedrockCloud.getLogger().info("§aYou are using the latest stable version.");
                }
            } catch (IOException e){
                BedrockCloud.getLogger().exception(e);
            }

            Thread.sleep(3000);
            new BedrockCloud();
        }catch (ClassNotFoundException ex) {
            BedrockCloud.getLogger().exception(ex);
        } catch (InterruptedException ignored) {
        }
    }

    private static String checkForUpdate() throws IOException {
        String url = "https://raw.githubusercontent.com/BedrockCloud/Cloud/master/src/main/resources/version.yml";
        InputStream inputStream = new URL(url).openStream();
        String content = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            content = bufferedReader.lines().reduce("", (x, y) -> x + y);
        }
        return content;
    }

    private static String getVersion() throws IOException {
        String content = checkForUpdate();
        Yaml yaml = new Yaml();
        Object obj = yaml.load(content);
        return (String) ((java.util.LinkedHashMap<?, ?>) obj).get("version");
    }

    private static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            return Integer.parseInt(version.substring(2, 3));
        }

        int index = version.indexOf(".");
        if (index != -1) {
            version = version.substring(0, index);
        }
        return Integer.parseInt(version);
    }

    private static boolean isLinux(){
        String osName = System.getProperty("os.name");
        return (osName.contains("Linux"));
    }
}
