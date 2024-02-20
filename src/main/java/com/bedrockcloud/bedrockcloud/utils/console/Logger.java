package com.bedrockcloud.bedrockcloud.utils.console;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.utils.Utils;

import java.io.*;

public class Logger
{
    private final File cloudLog;

    public Logger() {
        this.cloudLog = new File("./local/cloud.log");
    }

    public void info(final String message) {
        this.log("§aINFO", message);
    }

    public void error(final String message) {
        this.log("§cERROR", message);
    }

    public void debug(final String message) {
        this.log("§eDEBUG", message);
    }

    public void warning(final String message) {
        this.log("§6WARNING", message);
    }

    public void command(final String message) {
        this.log("§bCOMMAND", message);
    }

    public void exception(final Exception e) {
        if (!getStackTrace(e.getCause()).equals("Can't get stacktrace.")) this.log("§cEXCEPTION", getStackTrace(e.getCause()));
    }

    public static String getStackTrace(final Throwable t) {

        if (t == null) return "Can't get stacktrace.";

        final StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public void log(final String prefix, final String message) {
        System.out.println(Colors.toColor(BedrockCloud.getLoggerPrefix() + "§7[§r" + prefix + "§7]§r §8» §r" + message + "§r"));
        try (FileWriter cloudLogWriter = new FileWriter(this.cloudLog, true)) {
            File file = new File("./local/config.yml");
            if (!file.exists()) return;
            if (!Utils.getConfig().getBoolean("enable-cloudlog-file")) return;
            cloudLogWriter.append(Colors.removeColor(BedrockCloud.getLoggerPrefix() + "§7[§r" + prefix + "§7]§r §8» §r" + message + "§r")).append("\n");
            cloudLogWriter.flush();
        } catch (IOException ignored) {}
    }
}