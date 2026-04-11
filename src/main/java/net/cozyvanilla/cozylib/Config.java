package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private static PluginConfig config;
    private static DatabaseConfig database;
    private static RedisConfig redis;
    private static final Map<String, Boolean> modules = new HashMap<>();
    private static final Map<String, Boolean> integrations = new HashMap<>();

    public record PluginConfig(String prefix, String commandPrefix) {}
    public record DatabaseConfig(String host, int port, String user, String pass, String dbName) {}
    public record RedisConfig(String host, int port, String user, String password, boolean ssl) {}

    public Config(Plugin plugin) {
        YamlFileReader reader = new YamlFileReader(plugin, "config.yml");
        readConfig(reader);
    }

    private void readConfig(YamlFileReader reader) {
        config = new PluginConfig(
                reader.get().getString("plugin.prefix"),
                reader.get().getString("plugin.command_prefix"));

        database = new DatabaseConfig(
                reader.get().getString("mysql.host"),
                reader.get().getInt("mysql.port"),
                reader.get().getString("mysql.username"),
                reader.get().getString("mysql.password"),
                reader.get().getString("mysql.database"));

        // Redis
        redis = new RedisConfig(
                reader.get().getString("redis.host"),
                reader.get().getInt("redis.port"),
                reader.get().getString("redis.username"),
                reader.get().getString("redis.password"),
                reader.get().getBoolean("redis.ssl"));

        modules.putAll(reader.stringKeyBooleanMap("modules"));
        integrations.putAll(reader.stringKeyBooleanMap("integrations"));
    }

    public static String getPrefix() {
        return config.prefix();
    }

    public static String getCommandPrefix() {
        return config.commandPrefix();
    }

    public static DatabaseConfig getDatabaseConfig() {
        return database;
    }

    public static RedisConfig getRedisConfig() {
        return redis;
    }

    public static Map<String, Boolean> getModules() { return modules; }

    public static Map<String, Boolean> getIntegrations() { return integrations; }

    public static boolean isEnabled(String module) {
        return modules.getOrDefault(module, false);
    }
}