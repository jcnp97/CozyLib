//package net.cozyvanilla.cozylib.modules.player_data;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import net.cozyvanilla.cozylib.modules.Module;
//import net.cozyvanilla.cozylib.modules.util.Console;
//import net.cozyvanilla.cozylib.modules.mysql.repositories.PlayerAddressRepository;
//import net.cozyvanilla.cozylib.util.bukkit.PlayerUtils;
//import net.cozyvanilla.cozylib.util.paper.AsyncUtils;
//import org.bukkit.plugin.Plugin;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.net.InetSocketAddress;
//import java.time.Instant;
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
//public final class PlayerData implements Module<Void> {
//    private final PlayerAddressRepository database;
//    private final Plugin plugin;
//    private final Cache<UUID, PlayerData> cache = Caffeine.newBuilder()
//            .expireAfterAccess(10, TimeUnit.MINUTES).build();
//
//
//    public PlayerData(Plugin plugin) {
//        this.plugin   = plugin;
//        this.database = new PlayerAddressRepository(this, "cozylib_players");
//    }
//
//    @Override
//    public String getName() { return "PlayerData"; }
//
//    @Override
//    public String getPrefix() { return "[CozyLib-" + getName() + "]"; }
//
//    @Override
//    public Void getCommands() { return null; }
//
//    @Override
//    public void getConfig() {}
//
//    @Override
//    public void enable() {}
//
//    @Override
//    public void disable() { cache.invalidateAll(); }
//
//    // -------------------------------------------------------------------------
//    // Public API
//    // -------------------------------------------------------------------------
//
//    @NotNull
//    public CompletableFuture<@Nullable UUID> getPlayerUUID(@NotNull String name) {
//        return AsyncUtils.supplyAsync(plugin, () -> database.getUUIDByName(name));
//    }
//
//    @NotNull
//    public Set<String> getIPAssociatedNames(InetSocketAddress address) {
//        return database.getNamesByIP(address);
//    }
//
//    public void getOrCreate(org.bukkit.entity.Player player) {
//        UUID uuid = player.getUniqueId();
//        String name = player.getName();
//        InetSocketAddress address = player.getAddress();
//
//        if (address == null && player.isOnline()) {
//            Console.severe(getPrefix(), "Trying to add " + name + " to database but ip address is null!");
//            PlayerUtils.kick(player);
//            return;
//        }
//    }
//
//    public void add(org.bukkit.entity.Player player) {
//
//
//
//
//        CozyPlayer cozyPlayer = database.getOrCreate(uuid, name, address);
//        cache.put(uuid, cozyPlayer);
//    }
//
//    @NotNull
//    public CozyPlayer get(org.bukkit.entity.Player player) {
//        UUID uuid = player.getUniqueId();
//
//        CozyPlayer cached = cache.get(uuid);
//        if (cached != null) return cached;
//
//        String name = player.getName();
//        InetSocketAddress address = player.getAddress();
//
//        if (address == null) {
//            Console.severe("get() called for " + name + " but address is null — returning stub.");
//            return new CozyPlayer(name, Instant.now(), Instant.now(), Set.of());
//        }
//
//        CozyPlayer cozyPlayer = database.getOrCreate(uuid, name, address);
//        cache.put(uuid, cozyPlayer);
//        return cozyPlayer;
//    }
//
//    public void evict(org.bukkit.entity.Player player) {
//        cache.remove(player.getUniqueId());
//    }
//}