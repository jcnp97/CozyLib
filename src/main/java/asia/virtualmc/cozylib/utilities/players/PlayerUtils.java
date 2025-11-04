package asia.virtualmc.cozylib.utilities.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtils {

    /**
     * Checks whether a player with the specified UUID is currently online.
     *
     * @param uuid the UUID of the player to check
     * @return {@code true} if the player is online; {@code false} otherwise or if the UUID is {@code null}
     */
    public static boolean isOnline(UUID uuid) {
        if (uuid == null) return false;
        Player player = Bukkit.getPlayer(uuid);
        return player != null && player.isOnline();
    }

    /**
     * Retrieves the UUID of an online player by their exact username.
     * <p>
     * Returns {@code null} if the player is offline or not found.
     * </p>
     *
     * @param playerName the exact username of the player
     * @return the UUID of the online player, or {@code null} if the player is not online
     */
    public static UUID getOnlineUUID(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player != null && player.isOnline()) {
            return player.getUniqueId();
        }

        return null;
    }

    /**
     * Gets the last known username associated with the specified UUID.
     * <p>
     * This method will return the player's most recently known name,
     * even if the player is currently offline.
     * </p>
     *
     * @param uuid the UUID of the player
     * @return the player's name, or {@code null} if it cannot be resolved
     */
    public static String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    /**
     * Retrieves the UUID of a player by name, regardless of whether they are online.
     * <p>
     * This method uses Bukkit's offline player data to obtain the UUID
     * even for players who have previously joined but are not currently online.
     * </p>
     *
     * @param playerName the name of the player
     * @return the UUID of the offline player
     */
    public static UUID getOfflineUUID(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }

    /**
     * Retrieves the UUID of a player, checking both online and offline records.
     * <p>
     * If the player is online, their active UUID is returned.
     * Otherwise, Bukkit’s offline player data is queried to obtain their UUID.
     * </p>
     *
     * @param playerName the name of the player
     * @return the UUID of the player, whether online or offline
     */
    public static UUID getUUID(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player != null && player.isOnline()) {
            return player.getUniqueId();
        }

        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }
}
