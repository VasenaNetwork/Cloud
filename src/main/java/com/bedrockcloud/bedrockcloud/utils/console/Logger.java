package com.bedrockcloud.bedrockcloud.utils.console;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Logger {
    private final File cloudLog;

    public Logger() {
        this.cloudLog = new File("./local/cloud.log");
    }

    public void info(final String message) {
        log(LogLevel.INFO, message);
    }

    public void error(final String message) {
        log(LogLevel.ERROR, message);
    }

    public void debug(final String message) {
        log(LogLevel.DEBUG, message);
    }

    public void warning(final String message) {
        log(LogLevel.WARNING, message);
    }

    public void command(final String message) {
        log(LogLevel.COMMAND, message);
    }

    public void exception(final Exception e) {
        if (e != null) {
            log(LogLevel.EXCEPTION, getFullStackTrace(e));
        }
    }

    private static String getFullStackTrace(@NotNull final Throwable t) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            pw.println(t.toString());
            t.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return "Error getting full stacktrace: " + e.getMessage();
        }
    }


    private void log(final LogLevel level, final String message) {
        String formattedMessage = String.format("%s[%s] » %s",
                Cloud.prefix,
                level.getName(),
                message);
        System.out.println(Colors.toColor(formattedMessage));

        try (FileWriter cloudLogWriter = new FileWriter(this.cloudLog, true)) {
            File file = new File("./local/config.yml");
            if (!file.exists()) return;
            if (!Utils.getConfig().getBoolean("enable-log")) return;

            String plainMessage = Colors.removeColor(formattedMessage);
            cloudLogWriter.append(plainMessage).append("\n");
            cloudLogWriter.flush();
        } catch (IOException ignored) {}
    }

    public enum LogLevel {
        INFO("INFO"),
        ERROR("ERROR"),
        DEBUG("DEBUG"),
        WARNING("WARNING"),
        COMMAND("COMMAND"),
        EXCEPTION("EXCEPTION");

        private final String name;

        LogLevel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}