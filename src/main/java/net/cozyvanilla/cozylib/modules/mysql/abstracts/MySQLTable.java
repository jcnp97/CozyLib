package net.cozyvanilla.cozylib.modules.mysql.abstracts;

import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.mysql.MySQLDatabaseAPI;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class MySQLTable {
    private static final String TABLE_NAME_PATTERN = "^[a-zA-Z0-9_]+$";

    protected final String tableName;

    protected MySQLTable(@NotNull String tableName) {
        this.tableName = validateTableName(tableName);
    }

    public final void createTable() {
        try (Connection conn = MySQLDatabaseAPI.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getCreateTableStatement())) {
            stmt.execute();
        } catch (SQLException e) {
            Console.severe("Error creating table " + tableName + ": " + e.getMessage());
        }
        createTrigger();
    }

    protected final Connection getConnection() throws SQLException {
        return MySQLDatabaseAPI.getConnection();
    }

    protected abstract String getCreateTableStatement();
    protected abstract void createTrigger();

    private String validateTableName(String tableName) {
        if (!tableName.matches(TABLE_NAME_PATTERN)) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }

        return tableName;
    }
}