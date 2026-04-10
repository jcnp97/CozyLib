package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import org.bukkit.plugin.Plugin;

public class Config {
    private static PluginConfig config;
    private static DatabaseConfig database;
    private static RedisConfig redis;

    public record PluginConfig(String prefix, String commandPrefix) {}
    public record DatabaseConfig(String host, int port, String user, String pass, String dbName) {}
    public record RedisConfig(String host, int port, String user, String password, boolean ssl) {}

    public Config(Plugin plugin) {

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
    }

    public static  PluginConfig getConfig() {
        return config;
    }

    public static DatabaseConfig getDatabaseConfig() {
        return database;
    }

    public static RedisConfig getRedisConfig() {
        return redis;
    }
}