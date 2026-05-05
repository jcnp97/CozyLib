//package net.cozyvanilla.cozylib.modules.mysql.repositories;
//
//import net.cozyvanilla.cozylib.Logger;
//import net.cozyvanilla.cozylib.modules.util.Console;
//import net.cozyvanilla.cozylib.modules.mysql.abstracts.AbstractMySQL;
//import net.cozyvanilla.cozylib.modules.player_data.PlayerData;
//import net.cozyvanilla.cozylib.util.java.AddressUtils;
//import org.bukkit.plugin.Plugin;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.net.InetSocketAddress;
//import java.sql.*;
//import java.time.Instant;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//public class PlayerAddressRepository extends AbstractMySQL {
//    private final PlayerData playerData;
//
//    public PlayerAddressRepository(PlayerData playerData, Plugin plugin, @NotNull String tableName) {
//        super(plugin, tableName);
//        this.playerData = playerData;
//    }
//
//    @Override
//    protected String getTableColumns() {
//        return """
//                player_uuid CHAR(36) NOT NULL,
//                player_ip VARBINARY(16) NOT NULL,
//                player_name VARCHAR(255) NOT NULL,
//                first_join TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//                PRIMARY KEY (player_uuid, player_ip)
//                """;
//    }
//
//    @Override
//    protected void createTrigger() {
//        String triggerName = "prevent_updates_except_last_join_" + tableName;
//        String drop = "DROP TRIGGER IF EXISTS " + triggerName;
//        String create =
//                "CREATE TRIGGER " + triggerName + " " +
//                        "BEFORE UPDATE ON " + tableName + " " +
//                        "FOR EACH ROW " +
//                        "BEGIN " +
//                        "  IF NOT (" +
//                        "    NEW.player_uuid <=> OLD.player_uuid AND " +
//                        "    NEW.player_ip   <=> OLD.player_ip   AND " +
//                        "    NEW.player_name <=> OLD.player_name AND " +
//                        "    NEW.first_join  <=> OLD.first_join" +
//                        "  ) THEN " +
//                        "    SIGNAL SQLSTATE '45000' " +
//                        "    SET MESSAGE_TEXT = 'Only last_join can be updated'; " +
//                        "  END IF; " +
//                        "END";
//
//        try (Connection conn = getConnection();
//             Statement stmt  = conn.createStatement()) {
//            stmt.execute(drop);
//            stmt.execute(create);
//
//        } catch (SQLException e) {
//            Logger.severe("Failed to create trigger for " + tableName, e);
//        }
//    }
//
//    @NotNull
//    public PlayerData.CozyPlayer create(UUID uuid, InetSocketAddress address, String name) {
//        byte[] ipBytes = AddressUtils.toBytes(address);
//        if (ipBytes == null) {
//            throw new IllegalArgumentException("Could not convert address to bytes: " + address);
//        }
//
//        String sql = "INSERT INTO " + tableName +
//                " (player_uuid, player_ip, player_name) VALUES (?, ?, ?)";
//
//        try (Connection connection = getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql)) {
//
//            ps.setString(1, uuid.toString());
//            ps.setBytes(2, ipBytes);
//            ps.setString(3, name);
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            Console.severe(playerData.getPrefix(), "Failed to add new row for " + name + ": " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//
//        Instant now = Instant.now();
//        return new PlayerData.CozyPlayer(uuid, name, now, now, getAddresses(uuid));
//    }
//
//    @Nullable
//    public PlayerData.CozyPlayer get(UUID uuid, InetSocketAddress address) {
//        byte[] ipBytes = AddressUtils.toBytes(address);
//        if (ipBytes == null) {
//            Console.severe(playerData.getPrefix(), "Could not convert address to bytes: " + address);
//            return null;
//        }
//
//        try (Connection connection = getConnection();
//             PreparedStatement ps = connection.prepareStatement(
//                     "SELECT * FROM " + tableName + " WHERE player_uuid = ? AND player_ip = ?")) {
//
//            ps.setString(1, uuid.toString());
//            ps.setBytes(2, ipBytes);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (!rs.next()) {
//                    return null;
//                }
//
//                String name = rs.getString("player_name");
//                Instant firstJoin = rs.getTimestamp("first_join").toInstant();
//                updateLastJoin(uuid, ipBytes);
//                Instant lastJoin = Instant.now();
//
//                return new PlayerData.CozyPlayer(uuid, name, firstJoin, lastJoin, getAddresses(uuid));
//            }
//        } catch (SQLException e) {
//            Console.severe(playerData.getPrefix(),
//                    "Failed to fetch player data for " + uuid + ": " + e.getMessage());
//        }
//
//        return null;
//    }
//
//    @Nullable
//    public UUID getUUIDByName(String name) {
//        if (name == null || name.isEmpty()) return null;
//
//        String exactSql = "SELECT player_uuid FROM " + tableName +
//                " WHERE BINARY player_name = ? " +
//                " ORDER BY last_join DESC LIMIT 1";
//
//        UUID uuid = queryForUUID(exactSql, name);
//        if (uuid != null) {
//            return uuid;
//        }
//
//        String insensitiveSql = "SELECT player_uuid FROM " + tableName +
//                " WHERE LOWER(player_name) = LOWER(?) " +
//                " ORDER BY last_join DESC LIMIT 1";
//
//        return queryForUUID(insensitiveSql, name);
//    }
//
//    @NotNull
//    public Set<String> getNamesByIP(InetSocketAddress ip) {
//        byte[] bytes = AddressUtils.toBytes(ip);
//        if (bytes == null) {
//            Console.severe(playerData.getPrefix(), "IP lookup failed for address " + ip);
//            return new HashSet<>();
//        }
//
//        String sql = "SELECT DISTINCT player_name FROM " + tableName +
//                " WHERE player_ip = ?";
//        Set<String> names = new HashSet<>();
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setBytes(1, bytes);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) names.add(rs.getString("player_name"));
//            }
//        } catch (SQLException e) {
//            Console.severe(playerData.getPrefix(), "IP → name lookup failed: " + e.getMessage());
//        }
//
//        return names;
//    }
//
//    // -------------------------------------------------------------------------
//    // private methods
//    // -------------------------------------------------------------------------
//
//    private void updateLastJoin(UUID uuid, byte[] ipBytes) {
//        String sql =
//                "UPDATE " + tableName +
//                        " SET last_join = CURRENT_TIMESTAMP" +
//                        " WHERE player_uuid = ? AND player_ip = ?";
//
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, uuid.toString());
//            ps.setBytes(2, ipBytes);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            Console.severe(playerData.getPrefix(), "Failed to update last_join for " + uuid + ": " + e.getMessage());
//        }
//    }
//
//    private Set<InetSocketAddress> getAddresses(UUID uuid) {
//        Set<InetSocketAddress> addresses = new HashSet<>();
//
//        String sql = "SELECT player_ip FROM " + tableName + " WHERE player_uuid = ?";
//
//        try (Connection connection = getConnection();
//             PreparedStatement ps = connection.prepareStatement(sql)) {
//
//            ps.setString(1, uuid.toString());
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    byte[] ipBytes = rs.getBytes("player_ip");
//                    if (ipBytes != null) {
//                        addresses.add(AddressUtils.fromBytes(ipBytes));
//                    }
//                }
//            }
//
//        } catch (SQLException e) {
//            Console.severe(playerData.getPrefix(),
//                    "An error occurred when trying to get ip addresses of " + uuid + ": " + e.getMessage());
//        }
//
//        return addresses;
//    }
//
//    @Nullable
//    private UUID queryForUUID(String sql, String name) {
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, name);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) return UUID.fromString(rs.getString("player_uuid"));
//            }
//        } catch (SQLException e) {
//            Console.severe(playerData.getPrefix(), "UUID lookup failed for name '" + name + "': " + e.getMessage());
//        }
//
//        return null;
//    }
//}