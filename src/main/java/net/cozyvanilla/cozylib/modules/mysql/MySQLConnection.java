package net.cozyvanilla.cozylib.modules.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLConnection {
    private final String poolName;
    private final Config config;

    private HikariDataSource hikariDataSource;
    private record Config(String host, int port, String databaseName, String userName, String password) {}

    public MySQLConnection(@NotNull Plugin plugin, String host, int port, String databaseName, String userName, String password) {
        this.poolName = plugin.getName() + "-mysql";
        this.config = new Config(host, port, databaseName, userName, password);
        load();
    }

    private void load() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName(poolName);
            hikariConfig.setJdbcUrl(
                    "jdbc:mysql://" + config.host() + ":" + config.port() + "/" + config.databaseName()
                            + "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8"
            );
            hikariConfig.setUsername(config.userName());
            hikariConfig.setPassword(config.password());

            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(5);
            hikariConfig.setIdleTimeout(300000);
            hikariConfig.setConnectionTimeout(10000);
            hikariConfig.setMaxLifetime(1800000);

            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            this.hikariDataSource = new HikariDataSource(hikariConfig);

            try (Connection ignored = hikariDataSource.getConnection()) {
                Console.info("Successfully initialized a connection: " + poolName);
            }

        } catch (Exception e) {
            Console.severe("Failed to initialize connection " + poolName + ": " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (hikariDataSource == null || hikariDataSource.isClosed()) {
            throw new SQLException("[" + poolName + "] Database pool is not initialized or already closed.");
        }

        return hikariDataSource.getConnection();
    }

    public void closeConnection() {
        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
        }
    }
}