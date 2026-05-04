//package net.cozyvanilla.cozylib.modules.mysql.generic;
//
//import net.cozyvanilla.cozylib.modules.messages.Console;
//import net.cozyvanilla.cozylib.modules.mysql.MySQLConnection;
//import net.cozyvanilla.cozylib.util.paper.AsyncUtils;
//import org.bukkit.plugin.Plugin;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//public class IntegerKeyDatabase {
//    private final Plugin plugin;
//    private final String tableName;
//    private final MySQLConnection connection;
//
//    public IntegerKeyDatabase(@NotNull Plugin plugin,
//                              @NotNull String tableName,
//                              @NotNull MySQLConnection connection,
//                              @NotNull List<String> dataList) {
//        this.plugin = plugin;
//        this.tableName = tableName;
//        this.connection = connection;
//    }
//
//    private String generateDefStmt() {
//        return "CREATE TABLE IF NOT EXISTS " + tableName + "_def (" +
//                "data_id INT NOT NULL AUTO_INCREMENT," +
//                "data_name VARCHAR(255) NOT NULL," +
//                "PRIMARY KEY (data_id)," +
//                "UNIQUE KEY (data_name)" +
//                ")";
//    }
//
//    private String generateDataStmt() {
//        return "CREATE TABLE IF NOT EXISTS " + tableName + "_data (" +
//                "player_uuid CHAR(36) NOT NULL," +
//                "data_id INT NOT NULL," +
//                "amount INT NOT NULL DEFAULT 0," +
//                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
//                "PRIMARY KEY (player_uuid, data_id)," +
//                "CONSTRAINT fk_" + tableName + "_data_definition " +
//                "FOREIGN KEY (data_id) REFERENCES " + tableName + "_def (data_id) " +
//                "ON DELETE CASCADE ON UPDATE CASCADE" +
//                ")";
//    }
//
//    private void generateTables(List<String> dataList) {
//        AsyncUtils.async(plugin, () -> syncGenerateTables(dataList));
//    }
//
//    private void syncGenerateTables(List<String> dataList) {
//        try (Connection conn = connection.getConnection()) {
//            try (PreparedStatement stmt = conn.prepareStatement(generateDefStmt())) {
//                stmt.execute();
//            }
//
//            try (PreparedStatement stmt = conn.prepareStatement(generateDataStmt())) {
//                stmt.execute();
//            }
//
//            // Insert new data types if they don't exist
//            String insertQuery = "INSERT IGNORE INTO " + tableName + "_def (data_name) VALUES (?)";
//
//            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
//                for (String data : dataList) {
//                    insertStmt.setString(1, data);
//                    insertStmt.addBatch();
//                }
//                insertStmt.executeBatch();
//            }
//        } catch (SQLException e) {
//            Console.severe("An error occurred when trying to initialize data for " + tableName + ": " + e.getMessage());
//        }
//    }
//
//    public Map<Integer, Integer> syncQuery(UUID uuid) {
//        Map<Integer, Integer> result = new HashMap<>();
//        String insertMissingRows =
//                "INSERT IGNORE INTO " + tableName + "_data (player_uuid, data_id, amount) " +
//                        "SELECT ?, d.data_id, 0 FROM " + tableName + "_def d";
//
//        String selectQuery =
//                "SELECT data_id, amount FROM " + tableName + "_data WHERE player_uuid = ?";
//
//        try (Connection conn = connection.getConnection()) {
//            // Ensure the player has rows for every known data_id
//            try (PreparedStatement insertStmt = conn.prepareStatement(insertMissingRows)) {
//                insertStmt.setString(1, uuid.toString());
//                insertStmt.executeUpdate();
//            }
//
//            // Read back all values
//            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
//                selectStmt.setString(1, uuid.toString());
//
//                try (ResultSet rs = selectStmt.executeQuery()) {
//                    while (rs.next()) {
//                        result.put(rs.getInt("data_id"), rs.getInt("amount"));
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            Console.severe("An error occurred when trying to query data for " + uuid + " in " + tableName + ": " + e.getMessage());
//        }
//
//        return result;
//    }
//
//    public void update(UUID uuid, Map<Integer, Integer> data) {
//        AsyncUtils.async(plugin, () -> syncUpdate(uuid, data));
//    }
//
//    public void syncUpdate(UUID uuid, Map<Integer, Integer> data) {
//        String upsertQuery =
//                "INSERT INTO " + tableName + "_data (player_uuid, data_id, amount) VALUES (?, ?, ?) " +
//                        "ON DUPLICATE KEY UPDATE amount = VALUES(amount)";
//
//        try (Connection conn = connection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(upsertQuery)) {
//
//            for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
//                stmt.setString(1, uuid.toString());
//                stmt.setInt(2, entry.getKey());
//                stmt.setInt(3, entry.getValue());
//                stmt.addBatch();
//            }
//            stmt.executeBatch();
//        } catch (SQLException e) {
//            Console.severe("An error occurred when trying to update data for " + uuid + " in " + tableName + ": " + e.getMessage());
//        }
//    }
//}