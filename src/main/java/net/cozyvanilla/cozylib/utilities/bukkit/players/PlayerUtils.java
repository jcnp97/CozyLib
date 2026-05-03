package net.cozyvanilla.cozylib.utilities.bukkit.players;

import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.utilities.messages.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerUtils {

    /**
     * Gets an online player by name.
     *
     * @param name the player name to search for
     * @return the player, or null if not online
     */
    @Nullable
    public Player getOnlinePlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null && player.isOnline()) {
            return player;
        }

        return null;
    }

    /**
     * Checks if a player with the given name is online.
     *
     * @param name the player name to check
     * @return true if the player is online
     */
    public static boolean isOnline(String name) {
        Player player = Bukkit.getPlayer(name);
        return player != null && player.isOnline();
    }

    /**
     * Gets the UUID of an online player by exact name.
     *
     * @param name the exact player name to search for
     * @return the player's UUID, or null if not online
     */
    @Nullable
    public static UUID getOnlineUUID(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null && player.isOnline()) {
            return player.getUniqueId();
        }

        return null;
    }

    /**
     * Gets the name of an online player by UUID.
     *
     * @param uuid the player UUID to search for
     * @return the player's name, or null if not online
     */
    @Nullable
    public static String getOnlineName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            return player.getName();
        }

        return null;
    }

    /**
     * Gets a cached offline player by name.
     *
     * @param name the player name to search for
     * @return the player, or null if not cached or not online
     */
    @Nullable
    public static Player getOfflinePlayer(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
        if (player != null) {
            return player.getPlayer();
        }

        return null;
    }

    /**
     * Gets the UUID of a cached offline player by name.
     *
     * @param name the player name to search for
     * @return the player's UUID, or null if not cached
     */
    @Nullable
    public static UUID getOfflineUUID(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
        if (player != null) {
            return player.getUniqueId();
        }

        return null;
    }

    /**
     * Gets all currently online players as a mutable set.
     *
     * @return a set of online players
     */
    public static Set<Player> getOnlinePlayers() {
        return new HashSet<>(Set.copyOf(Bukkit.getOnlinePlayers()));
    }

    /**
     * Gets the UUIDs of all currently online players.
     *
     * @return a set of online player UUIDs
     */
    public static Set<UUID> getOnlineUUIDs() {
        return getOnlinePlayers().stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toSet());
    }

    /**
     * Kicks a player with a predefined error message.
     *
     * @param player the player to kick
     */
    public static void kick(@NotNull Player player) {
        // todo: add kicking log here
        player.kick(AdventureUtils.toComponent("<red>[CozyLib] We have detected an unusual error. If the issue persists, please contact the administrator."));
    }
}