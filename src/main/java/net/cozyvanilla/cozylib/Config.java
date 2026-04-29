package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private static PluginConfig config;
    private static final Map<String, Boolean> modules = new HashMap<>();
    private static final Map<String, Boolean> integrations = new HashMap<>();

    public record PluginConfig(String prefix, String commandPrefix) {}

    public Config(Plugin plugin) {
        YamlFileReader reader = new YamlFileReader(plugin, "config.yml");
        readConfig(reader);
    }

    private void readConfig(YamlFileReader reader) {
        config = new PluginConfig(
                reader.get().getString("plugin.prefix"),
                reader.get().getString("plugin.command_prefix"));

        modules.putAll(reader.stringKeyBooleanMap("modules"));
        integrations.putAll(reader.stringKeyBooleanMap("integrations"));
    }

    public static String getName() { return "CozyLib"; }

    public static String getPrefix() {
        return config.prefix();
    }

    public static String getCommandPrefix() {
        return config.commandPrefix();
    }

    public static Map<String, Boolean> getModules() { return modules; }

    public static Map<String, Boolean> getIntegrations() { return integrations; }

    public static boolean hasModule(String module) {
        return modules.getOrDefault(module, false);
    }
    public static boolean hasIntegration(String module) {
        return integrations.getOrDefault(module, false);
    }
}