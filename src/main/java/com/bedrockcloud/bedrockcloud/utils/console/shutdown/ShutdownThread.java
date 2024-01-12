package com.bedrockcloud.bedrockcloud.utils.console.shutdown;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.api.event.cloud.CloudStartEvent;
import com.bedrockcloud.bedrockcloud.api.event.cloud.CloudStopEvent;

public class ShutdownThread extends Thread {

    @Override
    public void run() {
        CloudStopEvent event = new CloudStopEvent(BedrockCloud.getInstance());
        BedrockCloud.getInstance().getPluginManager().callEvent(event);

        BedrockCloud.getInstance().getPluginManager().disableAllPlugins();
        for (final String templateName : BedrockCloud.getTemplateProvider().getTemplateMap().keySet()) {
            if (BedrockCloud.getTemplateProvider().isTemplateRunning(BedrockCloud.getTemplateProvider().getTemplate(templateName))) {
                BedrockCloud.getTemplateProvider().getTemplate(templateName).stop();
            }
        }

        try {
            if (BedrockCloud.getNetworkManager().datagramSocket != null && !BedrockCloud.getNetworkManager().datagramSocket.isClosed()) {
                BedrockCloud.getNetworkManager().datagramSocket.close();
                BedrockCloud.getLogger().info("CloudSocket was closed.");
            }
        } catch (Exception ignored) {
        }
        BedrockCloud.getLogger().info("§aCloud is stopping now.");

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ignored) {
        }
    }
}