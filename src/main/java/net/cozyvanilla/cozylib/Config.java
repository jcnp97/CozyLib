package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Prefix prefix;
    private static Logger logger;
    private static final Map<String, Boolean> modules = new HashMap<>();
    private static final Map<String, Boolean> integrations = new HashMap<>();

    public record Prefix(String prefix, String commandPrefix) {}
    public record Logger(boolean file, boolean console, boolean isVerbose) {}

    public Config(Plugin plugin) {
        YamlFileReader reader = new YamlFileReader(plugin, "config.yml");
        readConfig(reader);
    }

    private void readConfig(YamlFileReader reader) {
        prefix = new Prefix(
                reader.get().getString("plugin.prefix"),
                reader.get().getString("plugin.command_prefix"));

        logger = new Logger(
                reader.get().getBoolean("logging.file"),
                reader.get().getBoolean("logging.console"),
                reader.get().getBoolean("logging.verbose"));

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

    public static Map<String, Boolean> getModules() { return modules; }

    public static Map<String, Boolean> getIntegrations() { return integrations; }

    public static boolean hasModule(String module) {
        return modules.getOrDefault(module, false);
    }
    public static boolean hasIntegration(String module) {
        return integrations.getOrDefault(module, false);
    }
}