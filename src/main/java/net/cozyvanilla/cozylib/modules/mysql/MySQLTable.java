package net.cozyvanilla.cozylib.modules.mysql;

import net.cozyvanilla.cozylib.modules.messages.Console;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class MySQLTable {
    private static final String TABLE_NAME_PATTERN = "^[a-zA-Z0-9_]+$";

    protected final MySQLConnection connection;
    protected final String tableName;

    protected MySQLTable(@NotNull MySQLConnection connection, @NotNull String tableName) {
        this.connection = connection;
        this.tableName = validateTableName(tableName);
    }

    public final void initialize() {
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getCreateTableStatement())) {
            stmt.execute();
        } catch (SQLException e) {
            Console.severe("Error creating table " + tableName + ": " + e.getMessage());
        }
    }

    protected abstract String getCreateTableStatement();

    private static String validateTableName(String tableName) {
        if (!tableName.matches(TABLE_NAME_PATTERN)) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        return tableName;
    }
}