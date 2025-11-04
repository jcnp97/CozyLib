package asia.virtualmc.cozylib.utilities.bukkit.messages;

import asia.virtualmc.cozylib.Enums;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBarUtils {

    /**
     * Sends an action bar message to the specified player using Adventure components.
     *
     * @param player  The player to send the message to.
     * @param message The message string to toComponent and send as an action bar.
     */
    public static void send(@NotNull Player player, String message) {
        if (player.isOnline()) {
            player.sendActionBar(AdventureUtils.toComponent(message));
        }
    }

    /**
     * Sends an action bar message to all online players.
     * <p>
     * The message is converted to an Adventure {@link Component} before being displayed.
     * </p>
     *
     * @param message the message text to display in the action bar
     */
    public static void sendAll(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(AdventureUtils.toComponent(message));
        }
    }

    /**
     * Sends a typed action bar message to a specific player.
     * <p>
     * The message is prefixed with the color or style corresponding to the specified
     * {@link Enums.MessageType}, retrieved via {@link MessageUtils#getColor(Enums.MessageType)}.
     * </p>
     *
     * @param player  the player to receive the action bar message
     * @param message the message text to display
     * @param type    the type of message (INFO, WARNING, SEVERE, or NOTIFY)
     */
    public static void send(@NotNull Player player, String message, Enums.MessageType type) {
        if (player.isOnline()) {
            player.sendActionBar(AdventureUtils.toComponent(MessageUtils.getColor(type) + message));
        }
    }

    /**
     * Sends a typed action bar message to all online players.
     * <p>
     * The message is prefixed with the color or style associated with the specified
     * {@link Enums.MessageType} and displayed in each player's action bar.
     * </p>
     *
     * @param message the message text to display
     * @param type    the type of message (INFO, WARNING, SEVERE, or NOTIFY)
     */
    public static void sendAll(String message, Enums.MessageType type) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(AdventureUtils.toComponent(MessageUtils.getColor(type) + message));
        }
    }
}