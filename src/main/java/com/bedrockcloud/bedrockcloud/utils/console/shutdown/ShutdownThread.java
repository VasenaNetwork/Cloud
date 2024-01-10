package com.bedrockcloud.bedrockcloud.utils.console.shutdown;

import com.bedrockcloud.bedrockcloud.BedrockCloud;

public class ShutdownThread extends Thread {

    @Override
    public void run() {
        BedrockCloud.getInstance().getPluginManager().disableAllPlugins();

        boolean servicesRunning = true;
        BedrockCloud.getLogger().info("§cPlease wait until all services are stopped.");
        for (final String templateName : BedrockCloud.getTemplateProvider().getTemplateMap().keySet()) {
            if (BedrockCloud.getTemplateProvider().isTemplateRunning(BedrockCloud.getTemplateProvider().getTemplate(templateName))) {
                BedrockCloud.getTemplateProvider().getTemplate(templateName).stop();
            }
        }

        while (servicesRunning) {
            int serverCount = BedrockCloud.getCloudServerProvider().getCloudServers().size();
            if (serverCount == 0){
                servicesRunning = false;
                BedrockCloud.setRunning(false);

                try {
                    if (BedrockCloud.getNetworkManager().datagramSocket != null && !BedrockCloud.getNetworkManager().datagramSocket.isClosed()) {
                        BedrockCloud.getNetworkManager().datagramSocket.close();
                        BedrockCloud.getLogger().info("CloudSocket was closed.");
                    }
                } catch (Exception ignored) {}
                BedrockCloud.getLogger().info("§aCloud is stopping now.");
            }
        }
    }
}