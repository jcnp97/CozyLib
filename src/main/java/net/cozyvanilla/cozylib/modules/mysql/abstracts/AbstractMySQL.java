package net.cozyvanilla.cozylib.modules.mysql.abstracts;

import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.runtime.MySQLConnection;
import net.cozyvanilla.cozylib.util.paper.FutureUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractMySQL {
    private static final String TABLE_NAME_PATTERN = "^[a-zA-Z0-9_]+$";

    protected final Plugin plugin;
    protected final String tableName;

    protected AbstractMySQL(@NotNull Plugin plugin, @NotNull String tableName) {
        this.plugin = plugin;
        this.tableName = validateTableName(tableName);
    }

    public final void initializeAsync() {
        FutureUtils.async(plugin, () -> {
            createTable();
            createTrigger();
        });
    }

    // ------------ usable methods ------------
    protected final Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

    // ------------ mandatory methods ------------
    protected abstract String getTableColumns();

    // ------------ optional methods ------------
    protected void createTrigger() {}

    // ------------ private methods ------------
    private void createTable() {
        String columns = getTableColumns().trim();

        if (columns.endsWith(",")) {
            columns = columns.substring(0, columns.length() - 1);
        }

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                columns + "," +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                "ON UPDATE CURRENT_TIMESTAMP" +
                ")";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            Logger.severe("Failed to create table " + tableName, e);
        }
    }

    private String validateTableName(String tableName) {
        if (!tableName.matches(TABLE_NAME_PATTERN)) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }

        return tableName;
    }
}