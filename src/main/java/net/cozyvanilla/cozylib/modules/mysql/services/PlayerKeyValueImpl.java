package net.cozyvanilla.cozylib.modules.mysql.services;

import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.mysql.MySQLDatabaseAPI;
import net.cozyvanilla.cozylib.modules.mysql.abstracts.AbstractMySQLTable;
import net.cozyvanilla.cozylib.modules.mysql.interfaces.PlayerKeyValueService;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerKeyValueImpl extends AbstractMySQLTable implements PlayerKeyValueService {
    private final Set<String> dataList;

    public PlayerKeyValueImpl(@NotNull String tableName, @NotNull Set<String> dataList) {
        super(tableName);
        this.dataList = dataList;
    }

    @Override
    protected String getCreateTableStatement() {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "player_uuid CHAR(36) NOT NULL," +
                "data_name VARCHAR(255) NOT NULL," +
                "amount INT NOT NULL DEFAULT 0," +
                "obtained_at TIMESTAMP NULL DEFAULT NULL," +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "PRIMARY KEY (player_uuid, data_name)" +
                ")";
    }

    @Override
    public Map<String, Integer> getData(UUID uuid) {
        Map<String, Integer> result = new HashMap<>();

        String insertMissingRows =
                "INSERT IGNORE INTO " + tableName + " (player_uuid, data_name, amount) VALUES (?, ?, 0)";

        String selectQuery =
                "SELECT data_name, amount FROM " + tableName + " WHERE player_uuid = ?";

        try (Connection conn = MySQLDatabaseAPI.getConnection()) {
            try (PreparedStatement insertStmt = conn.prepareStatement(insertMissingRows)) {
                for (String data : dataList) {
                    insertStmt.setString(1, uuid.toString());
                    insertStmt.setString(2, data);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, uuid.toString());

                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        result.put(rs.getString("data_name"), rs.getInt("amount"));
                    }
                }
            }
        } catch (SQLException e) {
            Console.severe("Error querying data for " + uuid + " in " + tableName + ": " + e.getMessage());
        }

        return result;
    }

    @Override
    public void updateData(UUID uuid, Map<String, Integer> data) {
        String upsertQuery =
                "INSERT INTO " + tableName + " (player_uuid, data_name, amount) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE amount = VALUES(amount)";

        try (Connection conn = MySQLDatabaseAPI.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upsertQuery)) {

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, entry.getKey());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            Console.severe("Error updating data for " + uuid + " in " + tableName + ": " + e.getMessage());
        }
    }

    @Override
    public void setObtainedAt(UUID uuid, String dataName) {
        String insertMissingRow =
                "INSERT IGNORE INTO " + tableName + " (player_uuid, data_name, amount) VALUES (?, ?, 0)";

        String updateQuery =
                "UPDATE " + tableName + " SET obtained_at = CURRENT_TIMESTAMP " +
                        "WHERE player_uuid = ? AND data_name = ? AND obtained_at IS NULL";

        try (Connection conn = MySQLDatabaseAPI.getConnection()) {
            try (PreparedStatement insertStmt = conn.prepareStatement(insertMissingRow)) {
                insertStmt.setString(1, uuid.toString());
                insertStmt.setString(2, dataName);
                insertStmt.executeUpdate();
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, uuid.toString());
                updateStmt.setString(2, dataName);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            Console.severe("Error setting obtained_at for " + uuid + " (" + dataName + ") in " + tableName + ": " + e.getMessage());
        }
    }

    @Override
    public Map<String, Instant> getObtainedAt(UUID uuid) {
        Map<String, Instant> result = new HashMap<>();

        String insertMissingRows =
                "INSERT IGNORE INTO " + tableName + " (player_uuid, data_name, amount) VALUES (?, ?, 0)";

        String selectQuery =
                "SELECT data_name, obtained_at FROM " + tableName + " WHERE player_uuid = ?";

        try (Connection conn = MySQLDatabaseAPI.getConnection()) {
            try (PreparedStatement insertStmt = conn.prepareStatement(insertMissingRows)) {
                for (String data : dataList) {
                    insertStmt.setString(1, uuid.toString());
                    insertStmt.setString(2, data);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setString(1, uuid.toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Timestamp ts = rs.getTimestamp("obtained_at");
                        result.put(rs.getString("data_name"), ts != null ? ts.toInstant() : null);
                    }
                }
            }
        } catch (SQLException e) {
            Console.severe("Error querying obtained_at for " + uuid + " in " + tableName + ": " + e.getMessage());
        }

        return result;
    }

    @Override
    public List<UUID> getByObtainedAt(String dataName, int count, Enums.Ordering order) {
        List<UUID> result = new ArrayList<>();

        String selectQuery =
                "SELECT player_uuid FROM " + tableName +
                        " WHERE data_name = ? AND obtained_at IS NOT NULL" +
                        " ORDER BY obtained_at " + order.name() +
                        " LIMIT ?";

        try (Connection conn = MySQLDatabaseAPI.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

            stmt.setString(1, dataName);
            stmt.setInt(2, count);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(UUID.fromString(rs.getString("player_uuid")));
                }
            }
        } catch (SQLException e) {
            Console.severe("Error querying obtained_at for " + dataName + " in " + tableName + ": " + e.getMessage());
        }

        return result;
    }
}