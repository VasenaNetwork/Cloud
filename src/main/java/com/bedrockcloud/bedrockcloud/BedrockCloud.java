package com.bedrockcloud.bedrockcloud;

import com.bedrockcloud.bedrockcloud.api.event.EventHandler;
import com.bedrockcloud.bedrockcloud.api.event.cloud.CloudStartEvent;
import com.bedrockcloud.bedrockcloud.api.plugin.PluginManager;
import com.bedrockcloud.bedrockcloud.server.cloudserver.CloudServerProvider;
import com.bedrockcloud.bedrockcloud.utils.ServerUtils;
import com.bedrockcloud.bedrockcloud.utils.ThreadFactoryBuilder;
import com.bedrockcloud.bedrockcloud.utils.command.CommandRegistry;
import com.bedrockcloud.bedrockcloud.utils.config.Config;
import com.bedrockcloud.bedrockcloud.utils.console.shutdown.ShutdownThread;
import com.bedrockcloud.bedrockcloud.network.packetRegistry.PacketRegistry;
import com.bedrockcloud.bedrockcloud.player.CloudPlayerProvider;
import com.bedrockcloud.bedrockcloud.rest.App;
import com.bedrockcloud.bedrockcloud.tasks.RestartTask;
import com.bedrockcloud.bedrockcloud.utils.console.reader.ConsoleReader;
import com.bedrockcloud.bedrockcloud.network.NetworkManager;
import com.bedrockcloud.bedrockcloud.network.handler.PacketHandler;
import com.bedrockcloud.bedrockcloud.templates.TemplateProvider;
import com.bedrockcloud.bedrockcloud.utils.console.Logger;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import com.bedrockcloud.bedrockcloud.utils.scheduler.Scheduler;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BedrockCloud
{
    @Getter
    private static BedrockCloud instance;

    @Getter
    private static TemplateProvider templateProvider;
    @Getter
    private static CloudServerProvider cloudServerProvider;
    @Getter
    private static CloudPlayerProvider cloudPlayerProvider;
    @Getter
    private static PacketHandler packetHandler;
    @Getter
    private static NetworkManager networkManager;
    @Getter
    private static ConsoleReader consoleReader;
    @Getter
    private static boolean running;
    @Getter
    private static Config maintenanceFile;
    @Getter
    private File pluginPath;

    private final PluginManager pluginManager;
    @Setter
    @Getter
    private EventHandler eventHandler;

    public final static String prefix = "§l§bCloud §r§8» §r";
    private Scheduler scheduler;
    private final ScheduledExecutorService tickExecutor;
    private final ScheduledFuture<?> tickFuture;
    private int currentTick = 0;
    
    public static Logger getLogger() {
        return new Logger();
    }

    public static String getLoggerPrefix() {
        return "§b" + CloudStarter.getCloudUser() + "§r@§b" + "cloud §r§8» §r";
    }

    public BedrockCloud() {
        instance = this;
        running = true;

        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        maintenanceFile = new Config("./local/maintenance.txt", Config.ENUM);
        this.pluginPath = new File("./local/plugins/cloud");
        this.pluginManager = new PluginManager(this);

        this.initProvider();

        CommandRegistry.registerAllCommands();
        BedrockCloud.networkManager = new NetworkManager((int) Utils.getConfig().getDouble("port"));

        if (Utils.getConfig().getBoolean("rest-enabled", true)) {
            new App();
        } else {
            BedrockCloud.getLogger().warning("§cRestAPI is currently disabled. You can enable it in your cloud config.");
        }

        getTemplateProvider().loadTemplates();

        final Timer restartTimer = new Timer();
        if (Utils.getConfig().getBoolean("auto-restart-cloud", false)) {
            restartTimer.schedule(new RestartTask(), 1000L, 1000L);
        }

        ThreadFactoryBuilder builder = ThreadFactoryBuilder
                .builder()
                .format("Cloud Executor - #%d")
                .build();
        this.tickExecutor = Executors.newScheduledThreadPool(1, builder);

        getPluginManager().enableAllPlugins();

        ServerUtils.startAllProxies();
        ServerUtils.startAllServers();
        BedrockCloud.networkManager.start();

        this.tickFuture = this.tickExecutor.scheduleAtFixedRate(this::tickProcessor, 50, 50, TimeUnit.MILLISECONDS);

        CloudStartEvent event = new CloudStartEvent(this);
        getPluginManager().callEvent(event);
    }

    private void initProvider() {
        this.scheduler = new Scheduler(this);
        BedrockCloud.consoleReader = new ConsoleReader();
        BedrockCloud.templateProvider = new TemplateProvider();
        BedrockCloud.cloudServerProvider = new CloudServerProvider();
        BedrockCloud.cloudPlayerProvider = new CloudPlayerProvider();
        BedrockCloud.packetHandler = new PacketHandler();
        
        PacketRegistry.registerPackets();
        BedrockCloud.consoleReader.start();
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @ApiStatus.Internal
    public static void setRunning(boolean running) {
        BedrockCloud.running = running;
    }

    private void tickProcessor() {
        if (!BedrockCloud.isRunning() && !this.tickFuture.isCancelled()) {
            this.tickFuture.cancel(false);
        }

        try {
            this.onTick(++this.currentTick);
        } catch (Exception e) {
            BedrockCloud.getLogger().exception(e);
        }
    }

    private void onTick(int currentTick) {
        this.scheduler.onTick(currentTick);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public int getCurrentTick() {
        return currentTick;
    }
}