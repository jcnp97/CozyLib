package net.cozyvanilla.cozylib.modules.mysql.services;

import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.mysql.abstracts.MySQLTable;
import net.cozyvanilla.cozylib.modules.mysql.interfaces.PlayerRepository;
import net.cozyvanilla.cozylib.modules.mysql.interfaces.ResultSetMapper;
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

public class PlayerKeyValueImpl extends MySQLTable implements PlayerRepository<Map<String, Integer>> {

    // Maps (data_name -> amount) from a result set row
    private static final ResultSetMapper<Map.Entry<String, Integer>> AMOUNT_MAPPER =
            rs -> Map.entry(rs.getString("data_name"), rs.getInt("amount"));

    // Maps (data_name -> obtained_at) from a result set row
    private static final ResultSetMapper<Map.Entry<String, Instant>> OBTAINED_AT_MAPPER = rs -> {
        Timestamp ts = rs.getTimestamp("obtained_at");
        return Map.entry(rs.getString("data_name"), ts != null ? ts.toInstant() : null);
    };

    private final Set<String> dataList;

    public PlayerKeyValueImpl(@NotNull String tableName, @NotNull Set<String> dataList) {
        super(tableName);
        this.dataList = dataList;
        createTable();
        createTrigger();
        prune();
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
    protected void createTrigger() {}

    @Override
    public @NotNull Map<String, Integer> getOrCreate(UUID uuid) {
        Map<String, Integer> result = new HashMap<>();

        String selectQuery = "SELECT data_name, amount FROM " + tableName + " WHERE player_uuid = ?";
        try (Connection conn = getConnection()) {
            insertMissingRows(conn, uuid);

            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, uuid.toString());

                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        Map.Entry<String, Integer> entry = AMOUNT_MAPPER.fromResultSet(rs);
                        result.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (SQLException e) {
            Console.severe("Error querying data for " + uuid + " in " + tableName + ": " + e.getMessage());
        }

        return result;
    }

    @Override
    public void update(UUID uuid, Map<String, Integer> data) {
        String upsertQuery =
                "INSERT INTO " + tableName + " (player_uuid, data_name, amount) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE amount = VALUES(amount)";

        try (Connection conn = getConnection();
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
    public void prune() {
        String deleteQuery =
                "DELETE FROM " + tableName +
                        " WHERE amount = 0" +
                        " AND obtained_at IS NULL" +
                        " AND last_updated <= NOW() - INTERVAL 7 DAY";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            Console.severe("Error pruning table " + tableName + ": " + e.getMessage());
        }
    }

    public void setObtainedAt(UUID uuid, String dataName) {
        String insertMissingRow =
                "INSERT IGNORE INTO " + tableName + " (player_uuid, data_name, amount) VALUES (?, ?, 0)";

        String updateQuery =
                "UPDATE " + tableName + " SET obtained_at = CURRENT_TIMESTAMP " +
                        "WHERE player_uuid = ? AND data_name = ? AND obtained_at IS NULL";

        try (Connection conn = getConnection()) {
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

    public Map<String, Instant> getObtainedAt(UUID uuid) {
        Map<String, Instant> result = new HashMap<>();

        String selectQuery = "SELECT data_name, obtained_at FROM " + tableName + " WHERE player_uuid = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

            stmt.setString(1, uuid.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map.Entry<String, Instant> entry = OBTAINED_AT_MAPPER.fromResultSet(rs);
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (SQLException e) {
            Console.severe("Error querying obtained_at for " + uuid + " in " + tableName + ": " + e.getMessage());
        }

        return result;
    }

    public List<UUID> getByObtainedAt(String dataName, int count, Enums.Ordering order) {
        List<UUID> result = new ArrayList<>();

        String selectQuery =
                "SELECT player_uuid FROM " + tableName +
                        " WHERE data_name = ? AND obtained_at IS NOT NULL" +
                        " ORDER BY obtained_at " + order.name() +
                        " LIMIT ?";

        try (Connection conn = getConnection();
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

    // Inserts default rows for any data keys not yet present for this player
    private void insertMissingRows(Connection conn, UUID uuid) throws SQLException {
        String insertMissingRows =
                "INSERT IGNORE INTO " + tableName + " (player_uuid, data_name, amount) VALUES (?, ?, 0)";

        try (PreparedStatement insertStmt = conn.prepareStatement(insertMissingRows)) {
            for (String data : dataList) {
                insertStmt.setString(1, uuid.toString());
                insertStmt.setString(2, data);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }
}