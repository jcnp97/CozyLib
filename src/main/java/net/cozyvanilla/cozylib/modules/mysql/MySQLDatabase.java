package net.cozyvanilla.cozylib.modules.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.cozyvanilla.cozylib.modules.Module;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLDatabase implements Module<Void> {
    private final Plugin plugin;
    private static HikariDataSource hikariDataSource;

    public MySQLDatabase(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "MySQL-Database";
    }

    @Override
    public String getPrefix() {
        return "[CozyMySQL]";
    }

    @Override
    public Void getCommands() {
        return null;
    }

    @Override
    public void enable() {
        MySQLDatabaseAPI.register(this);

        YamlFileReader file = new YamlFileReader(plugin, "modules/mysql-database.yml");
        hikariDataSource = getHikari(
                file.get().getString("mysql.pool_name"),
                file.get().getString("mysql.host"),
                file.get().getInt("mysql.port"),
                file.get().getString("mysql.username"),
                file.get().getString("mysql.password"),
                file.get().getString("mysql.database")
        );
    }

    @Override
    public void disable() {
        MySQLDatabaseAPI.unregister(this);
        closeConnection();
    }

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
            Console.severe("Failed to initialize MySQL Connection connection: " + e.getMessage());
        }

        return null;
    }

    private void closeConnection() {
        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        if (hikariDataSource == null || hikariDataSource.isClosed()) {
            Console.severe("MySQL connection pool is not initialized or closed.");
            throw new SQLException();
        }

        return hikariDataSource.getConnection();
    }
}
