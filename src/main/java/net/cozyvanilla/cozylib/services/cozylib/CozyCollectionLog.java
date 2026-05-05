//package net.cozyvanilla.cozylib.services.cozylib;
//
//import net.cozyvanilla.cozylib.modules.util.Console;
//import net.cozyvanilla.cozylib.modules.mysql.repositories.PlayerProgressRepository;
//import net.cozyvanilla.cozylib.util.paper.AsyncUtils;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
//import org.bukkit.event.player.PlayerQuitEvent;
//import org.bukkit.plugin.Plugin;
//
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//
//public class CozyCollectionLog implements Listener {
//    private final Plugin plugin;
//    private final PlayerProgressRepository database;
//    private final Map<UUID, Map<String, PlayerProgressRepository.Collection>> playerCache = new HashMap<>();
//    private final Set<UUID> dirtyPlayers =  new HashSet<>();
//
//    public CozyCollectionLog(Plugin plugin, Set<String> dataList) {
//        this.plugin = plugin;
//        String tableName = plugin.getName().toLowerCase() + "_collections";
//        this.database = new PlayerProgressRepository(tableName, dataList);
//    }
//
//    @EventHandler
//    public void onJoin(PlayerJoinEvent e) {
//        Player player = e.getPlayer();
//        UUID uuid = player.getUniqueId();
//        getOrCreate(uuid);
//    }
//
//    @EventHandler
//    public void onLeave(PlayerQuitEvent e) {
//        Player player = e.getPlayer();
//        UUID uuid = player.getUniqueId();
//        // store cached data to database
//        updateDatabase(uuid);
//    }
//
//    private void enable() {
//        database.initialize();
//        plugin.getServer().getPluginManager().registerEvents(this, plugin);
//    }
//
//    private void disable() {
//
//    }
//
//    public void getOrCreate(UUID uuid) {
//        database.getAsync(uuid)
//                .thenCompose(optionalData -> {
//                    if (optionalData.isPresent()) {
//                        playerCache.put(uuid, optionalData.get());
//                        return CompletableFuture.completedFuture(null);
//                    }
//
//                    // not found → create
//                    return database.createAsync(uuid).thenAccept(createdData -> {
//                        playerCache.put(uuid, createdData);
//                    });
//                })
//                .exceptionally(error -> {
//                    Throwable cause = error.getCause() == null ? error : error.getCause();
//
//                    plugin.getServer().getScheduler().runTask(plugin, () -> {
//                        Console.severe("[" + plugin.getName() + "] ",
//                                "Failed to getOrCreate data for " + uuid + ": " + cause.getMessage()
//                        );
//                    });
//
//                    return null;
//                });
//    }
//
//    // does nothing if cache data does not exist/empty
//    public void updateDatabase(UUID uuid) {
//        Map<String, Integer> data = getFromCache(uuid);
//        if (data == null || data.isEmpty()) { return; }
//        AsyncUtils.asyncAndForget(plugin, () -> {
//            database.update(uuid, data);
//        });
//    }
//
//    public Map<String, Integer> getFromCache(UUID uuid) {
//        return playerCache.getOrDefault(uuid, new HashMap<>());
//    }
//
//    public void markDirty(UUID uuid) {
//        dirtyPlayers.add(uuid);
//    }
//
//    public void saveAll() {
//        for (UUID uuid : dirtyPlayers) {
//            updateDatabase(uuid);
//        }
//
//        dirtyPlayers.clear();
//    }
//
//    //
//    public void incrementData(UUID uuid, String dataName, OperationType type, int amount) {
//        Map<String, Integer> data = getFromCache(uuid);
//        if (data == null || data.isEmpty()) { return; }
//
//        int value = data.getOrDefault(dataName, 0);
//        //data.replace(dataName, )
//    }
//}