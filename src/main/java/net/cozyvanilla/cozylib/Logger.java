package net.cozyvanilla.cozylib;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class Logger {
    private static volatile Plugin plugin;
    private static volatile File parentDirectory;

    private static final int LOG_RETENTION_DAYS = 30;

    private static final ExecutorService fileWriter =
            Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r, "CozyLib-Logger");
                thread.setDaemon(true);
                return thread;
            });

    public Logger(Plugin instance) {
        plugin = instance;
        parentDirectory = new File(plugin.getDataFolder(), "logs");

        purgeOldLogs();
    }

    public void disable() {
        fileWriter.shutdown();
        try {
            if (!fileWriter.awaitTermination(5, TimeUnit.SECONDS)) {
                fileWriter.shutdownNow();
            }
        } catch (InterruptedException e) {
            fileWriter.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // ------------ public methods ------------

    /**
     * Logs a severe-level message.
     *
     * @param message the message to log
     */
    public static void severe(String message) {
        log(Level.SEVERE, message, null);
    }

    /**
     * Logs a severe-level message with an associated throwable.
     *
     * @param message the message to log
     * @param throwable the exception to include in the log
     */
    public static void severe(String message, Throwable throwable) {
        log(Level.SEVERE, message, throwable);
    }

    /**
     * Logs a warning-level message.
     *
     * @param message the message to log
     */
    public static void warning(String message) {
        log(Level.WARNING, message, null);
    }

    /**
     * Logs an info-level message.
     *
     * @param message the message to log
     */
    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    // ------------ private methods ------------

    private static void log(Level level, String message, Throwable throwable) {
        Plugin currentPlugin = plugin;

        StackTraceElement caller = findCaller();
        String location = caller.getClassName() + "#" + caller.getMethodName()
                + ":" + caller.getLineNumber();

        String formatted = "[" + level.getName() + "] [" + location + "] " + message;

        if (Config.logToConsole() && currentPlugin != null) {
            currentPlugin.getLogger().log(level, formatted);

            if (Config.isVerbose() && throwable != null) {
                throwable.printStackTrace();
            }
        }

        if (Config.logToFile() && level == Level.SEVERE) {
            fileWriter.submit(() -> writeToFile(formatted, throwable));
        }
    }

    private static StackTraceElement findCaller() {
        String loggerClassName = Logger.class.getName();

        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (!element.getClassName().equals(loggerClassName)
                    && !element.getClassName().equals(Thread.class.getName())) {
                return element;
            }
        }

        return new StackTraceElement("unknown", "unknown", null, -1);
    }

    private static void writeToFile(String message, Throwable throwable) {
        File directory = parentDirectory;

        if (directory == null) {
            return;
        }

        try {
            Files.createDirectories(directory.toPath());

            File logFile = new File(directory, LocalDate.now() + ".log");

            StringBuilder out = new StringBuilder()
                    .append(LocalDateTime.now())
                    .append(" ")
                    .append(message)
                    .append(System.lineSeparator());

            if (Config.isVerbose() && throwable != null) {
                StringWriter sw = new StringWriter();
                throwable.printStackTrace(new PrintWriter(sw));
                out.append(sw);
            }

            Files.writeString(
                    logFile.toPath(),
                    out.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            Plugin currentPlugin = plugin;
            if (currentPlugin != null) {
                currentPlugin.getLogger().severe("Failed to write to log file: " + e.getMessage());
            }
        }
    }

    private static void purgeOldLogs() {
        File directory = parentDirectory;

        if (directory == null || !directory.exists()) {
            return;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".log"));

        if (files == null) {
            return;
        }

        LocalDate today = LocalDate.now();

        for (File file : files) {
            try {
                String fileName = file.getName();
                String datePart = fileName.substring(0, fileName.length() - 4);
                LocalDate logDate = LocalDate.parse(datePart);

                long age = ChronoUnit.DAYS.between(logDate, today);

                if (age > LOG_RETENTION_DAYS) {
                    Files.deleteIfExists(file.toPath());
                }
            } catch (Exception ignored) {
                // Ignore files not named like yyyy-MM-dd.log
            }
        }
    }
}