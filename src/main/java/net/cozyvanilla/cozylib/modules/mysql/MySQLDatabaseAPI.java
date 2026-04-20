package net.cozyvanilla.cozylib.modules.mysql;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLDatabaseAPI {

    private static MySQLDatabase instance;

    private MySQLDatabaseAPI() {}

    static void register(@NotNull MySQLDatabase mysql) {
        instance = mysql;
    }

    static void unregister(@NotNull MySQLDatabase mysql) {
        instance = null;
    }

    private static @NotNull MySQLDatabase require() {
        if (instance == null) {
            throw new IllegalStateException("MySQL-Database module is not initialized");
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException { return require().getConnection(); }
}
