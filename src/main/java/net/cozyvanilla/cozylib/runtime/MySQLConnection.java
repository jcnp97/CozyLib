package net.cozyvanilla.cozylib.runtime;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.modules.util.Console;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLConnection {
    private final Plugin plugin;
    private static HikariDataSource hikariDataSource;

    public MySQLConnection(Plugin plugin) {
        this.plugin = plugin;
        enable();
    }

    public void enable() {
        Config.MySQLConfig config = Config.getMySQLConfig();
        hikariDataSource = getHikari(
                config.poolName(),
                config.host(),
                config.port(),
                config.username(),
                config.password(),
                config.dbName());

        if (hikariDataSource != null) {
            return;
        }

        Console.print("This plugin requires a MySQL Database. Please make sure to provide valid credentials on config.yml!", MessageType.INFO);
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    public void disable() { closeConnection(); }

    private HikariDataSource getHikari(String poolName, String host, int port, String user, String pass, String dbName) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName(poolName);
            hikariConfig.setJdbcUrl(
                    "jdbc:mysql://" + host + ":" + port + "/" + dbName
                            + "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8"
            );
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(pass);

            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(5);
            hikariConfig.setIdleTimeout(300000);
            hikariConfig.setConnectionTimeout(10000);
            hikariConfig.setMaxLifetime(1800000);

            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            return new HikariDataSource(hikariConfig);

        } catch (Exception e) {
            Logger.severe("Failed to initialize MySQL connection: ", e);
        }

        return null;
    }

    private void closeConnection() {
        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (hikariDataSource == null || hikariDataSource.isClosed()) {
            Logger.severe("MySQL connection pool is not initialized or closed.");
            throw new SQLException();
        }

        return hikariDataSource.getConnection();
    }
}
