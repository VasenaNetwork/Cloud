package com.bedrockcloud.bedrockcloud.utils.console.shutdown;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.api.event.cloud.CloudStopEvent;

public class ShutdownThread extends Thread {

    @Override
    public void run() {
        CloudStopEvent event = new CloudStopEvent(Cloud.getInstance());
        Cloud.getInstance().getPluginManager().callEvent(event);

        Cloud.getInstance().getPluginManager().disableAllPlugins();
        for (final String templateName : Cloud.getTemplateProvider().getTemplateMap().keySet()) {
            if (Cloud.getTemplateProvider().isTemplateRunning(Cloud.getTemplateProvider().getTemplate(templateName))) {
                Cloud.getTemplateProvider().getTemplate(templateName).stop();
            }
        }

        try {
            if (Cloud.getNetworkManager().getDatagramSocket() != null && !Cloud.getNetworkManager().getDatagramSocket().isClosed()) {
                Cloud.getNetworkManager().getDatagramSocket().close();
                Cloud.getLogger().info("CloudSocket was closed.");
            }
        } catch (Exception ignored) {
        }
        Cloud.getLogger().info("§aCloud is stopping now.");

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ignored) {
        }
    }
}