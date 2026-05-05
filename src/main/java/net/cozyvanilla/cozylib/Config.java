package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.util.yaml.YamlReader;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Prefix prefix;
    private static Logger logger;

    private static MessageColor messageColor;
    private static MessageIcon messageIcon;
    private static MessageSound messageSound;

    private static MySQLConfig mySQLConfig;

    private static final Map<String, Boolean> modules = new HashMap<>();
    private static final Map<String, Boolean> integrations = new HashMap<>();

    public record Prefix(String prefix, String commandPrefix) {}
    public record Logger(boolean file, boolean console, boolean isVerbose) {}

    public record MessageColor(String info, String warning, String severe, String notification, String broadcast) {}
    public record MessageIcon(String info, String warning, String severe, String notification, String broadcast) {}
    public record MessageSound(String info, String warning, String severe, String notification, String broadcast) {}

    public record MySQLConfig(String poolName, String host, int port, String username, String password, String dbName) {}

    public Config(Plugin plugin) {
        YamlReader reader = new YamlReader(plugin, "config.yml");
        readConfig(reader);
    }

    private void readConfig(YamlReader reader) {
        prefix = new Prefix(
                reader.get().getString("plugin.prefix"),
                reader.get().getString("plugin.command_prefix"));

        logger = new Logger(
                reader.get().getBoolean("logging.file"),
                reader.get().getBoolean("logging.console"),
                reader.get().getBoolean("logging.verbose"));

        messageColor = new MessageColor(
                reader.get().getString("messages.color.info"),
                reader.get().getString("messages.color.warning"),
                reader.get().getString("messages.color.severe"),
                reader.get().getString("messages.color.notification"),
                reader.get().getString("messages.color.broadcast")
                );

        messageIcon = new MessageIcon(
                reader.get().getString("messages.icon.info"),
                reader.get().getString("messages.icon.warning"),
                reader.get().getString("messages.icon.severe"),
                reader.get().getString("messages.icon.notification"),
                reader.get().getString("messages.icon.broadcast")
        );

        messageSound = new MessageSound(
                reader.get().getString("messages.sound.info"),
                reader.get().getString("messages.sound.warning"),
                reader.get().getString("messages.sound.severe"),
                reader.get().getString("messages.sound.notification"),
                reader.get().getString("messages.sound.broadcast")
        );

        mySQLConfig = new MySQLConfig(
                reader.get().getString("mysql.pool_name"),
                reader.get().getString("mysql.host"),
                reader.get().getInt("mysql.port"),
                reader.get().getString("mysql.username"),
                reader.get().getString("mysql.password"),
                reader.get().getString("mysql.database")
        );

        modules.putAll(reader.stringKeyBooleanMap("modules"));
        integrations.putAll(reader.stringKeyBooleanMap("integrations"));
    }

    public static String getName() { return "CozyLib"; }

    public static String getPrefix() {
        return prefix.prefix();
    }

    public static String getCommandPrefix() {
        return prefix.commandPrefix();
    }

    public static boolean logToConsole() { return logger.console(); }

    public static boolean logToFile() { return logger.file(); }

    public static boolean isVerbose() { return logger.isVerbose(); }

    public static String getColor(MessageType type) {
        return switch (type) {
            case INFO -> messageColor.info();
            case WARNING -> messageColor.warning();
            case SEVERE -> messageColor.severe();
            case NOTIFICATION -> messageColor.notification();
            case BROADCAST -> messageColor.broadcast();
        };
    }

    public static String getIcon(MessageType type) {
        return switch (type) {
            case INFO -> messageIcon.info();
            case WARNING -> messageIcon.warning();
            case SEVERE -> messageIcon.severe();
            case NOTIFICATION -> messageIcon.notification();
            case BROADCAST -> messageIcon.broadcast();
        };
    }

    public static String getSound(MessageType type) {
        return switch (type) {
            case INFO -> messageSound.info();
            case WARNING -> messageSound.warning();
            case SEVERE -> messageSound.severe();
            case NOTIFICATION -> messageSound.notification();
            case BROADCAST -> messageSound.broadcast();
        };
    }

    public static MySQLConfig getMySQLConfig() { return mySQLConfig; }

    public static Map<String, Boolean> getModules() { return modules; }

    public static Map<String, Boolean> getIntegrations() { return integrations; }

    public static boolean hasModule(String module) {
        return modules.getOrDefault(module, false);
    }

    public static boolean hasIntegration(String module) {
        return integrations.getOrDefault(module, false);
    }
}