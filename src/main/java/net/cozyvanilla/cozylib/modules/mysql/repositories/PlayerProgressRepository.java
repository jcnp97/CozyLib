package net.cozyvanilla.cozylib.modules.mysql.repositories;

import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.modules.mysql.abstracts.AbstractMySQL;
import net.cozyvanilla.cozylib.modules.mysql.interfaces.PlayerRepository;
import net.cozyvanilla.cozylib.util.paper.FutureUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerProgressRepository extends AbstractMySQL implements PlayerRepository<Map<String, PlayerProgressRepository.Collection>> {

    private final Set<String> dataList;
    public record Collection(int amount, @Nullable Instant obtainedAt) {}

    public PlayerProgressRepository(@NotNull Plugin plugin, @NotNull String tableName, @NotNull Set<String> dataList) {
        super(plugin, tableName);
        this.dataList = dataList;
        initializeAsync(); // -> table creation

        FutureUtils.handleAsync(updateMissingDataAsync(), value -> {
            if (value > 0) {
                Logger.info("Successfully updated (" + value + ") rows on " + tableName);
            }
        }, null);
    }

    @Override
    protected String getTableColumns() {
        return """
           player_uuid CHAR(36) NOT NULL,
           data_name VARCHAR(255) NOT NULL,
           amount INT NOT NULL DEFAULT 0,
           obtained_at TIMESTAMP NULL DEFAULT NULL,
           PRIMARY KEY (player_uuid, data_name)
           """;
    }

    @Override
    public @NotNull Optional<Map<String, Collection>> get(UUID uuid) {
        try {
            String sql = "SELECT data_name, amount, obtained_at FROM " + tableName +
                    " WHERE player_uuid = ?";

            Map<String, Collection> result = new HashMap<>();

            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, uuid.toString());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String dataName = rs.getString("data_name");
                        int amount = rs.getInt("amount");

                        Timestamp timestamp = rs.getTimestamp("obtained_at");
                        Instant obtainedAt = timestamp == null ? null : timestamp.toInstant();

                        result.put(dataName, new Collection(amount, obtainedAt));
                    }
                }
            }

            return Optional.of(result);

        } catch (SQLException e) {
            Logger.severe("Error querying data for " + uuid + " in " + tableName, e);
        }

        return Optional.empty();
    }

    @Override
    public @NotNull CompletableFuture<Optional<Map<String, Collection>>> getAsync(UUID uuid) {
        return FutureUtils.supplyAsync(plugin, () -> get(uuid));
    }

    @Override
    public @NotNull Map<String, Collection> create(UUID uuid) {
        Map<String, Collection> data = new HashMap<>();

        try {
            String sql = "INSERT INTO " + tableName +
                    " (player_uuid, data_name, amount, obtained_at) " +
                    " VALUES (?, ?, 0, NULL) " +
                    " ON DUPLICATE KEY UPDATE amount = amount";

            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                for (String dataName : dataList) {
                    ps.setString(1, uuid.toString());
                    ps.setString(2, dataName);
                    ps.addBatch();

                    data.put(dataName, new Collection(0, null));
                }

                ps.executeBatch();
            }

        } catch (SQLException e) {
            Logger.severe("Error creating data for " + uuid + " in " + tableName, e);
        }

        return data;
    }

    @Override
    public @NotNull CompletableFuture<Map<String, Collection>> createAsync(UUID uuid) {
        return FutureUtils.supplyAsync(plugin, () -> create(uuid));
    }

    @Override
    public void update(UUID uuid, @NotNull Map<String, Collection> data) {
        if (data.isEmpty()) return;

        try {
            String sql = "UPDATE " + tableName +
                    " SET amount = GREATEST(amount, ?) " +
                    " WHERE player_uuid = ? AND data_name = ?";

            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                for (Map.Entry<String, Collection> entry : data.entrySet()) {
                    String dataName = entry.getKey();
                    Collection collection = entry.getValue();

                    if (collection == null) continue;

                    ps.setInt(1, collection.amount());
                    ps.setString(2, uuid.toString());
                    ps.setString(3, dataName);
                    ps.addBatch();
                }

                ps.executeBatch();
            }

        } catch (SQLException e) {
            Logger.severe("Error updating data for " + uuid + " in " + tableName, e);
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> updateAsync(UUID uuid, @NotNull Map<String, Collection> data) {
        if (data.isEmpty()) return CompletableFuture.completedFuture(null);
        return FutureUtils.async(plugin, () -> update(uuid, data));
    }

    public void setObtainedAt(UUID uuid, String dataName, @NotNull Instant time) {
        String sql = "UPDATE " + tableName +
                " SET obtained_at = ? " +
                " WHERE player_uuid = ? AND data_name = ? " +
                " AND (obtained_at IS NULL OR obtained_at > ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            Timestamp ts = Timestamp.from(time);

            ps.setTimestamp(1, ts);
            ps.setString(2, uuid.toString());
            ps.setString(3, dataName);
            ps.setTimestamp(4, ts);

            ps.executeUpdate();

        } catch (SQLException e) {
            Logger.severe("Error updating obtained_at for " + uuid + " / " + dataName + " in " + tableName, e);
        }
    }

    public @NotNull CompletableFuture<Void> setObtainedAtAsync(UUID uuid, String dataName, @NotNull Instant time) {
        return FutureUtils.async(plugin, () -> setObtainedAt(uuid, dataName, time));
    }

    // private methods
    private int updateMissingData() {
        String sql = """
        INSERT INTO %s (player_uuid, data_name, amount, obtained_at)
        SELECT p.player_uuid, ?, 0, NULL
        FROM (SELECT DISTINCT player_uuid FROM %s) p
        LEFT JOIN %s t
          ON t.player_uuid = p.player_uuid AND t.data_name = ?
        WHERE t.player_uuid IS NULL
        """.formatted(tableName, tableName, tableName);

        int synced = 0;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (String dataName : dataList) {
                ps.setString(1, dataName);
                ps.setString(2, dataName);
                ps.addBatch();
            }

            int[] results = ps.executeBatch();

            for (int result : results) {
                if (result > 0) {
                    synced += result;
                } else if (result == Statement.SUCCESS_NO_INFO) {
                    synced++;
                }
            }

            return synced;

        } catch (SQLException e) {
            Logger.severe("Error syncing missing data for " + tableName, e);
            return 0;
        }
    }

    private CompletableFuture<Integer> updateMissingDataAsync() {
        return FutureUtils.supplyAsync(plugin, this::updateMissingData);
    }
}