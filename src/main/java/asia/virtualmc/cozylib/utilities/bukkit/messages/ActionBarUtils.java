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
    public static void send(Player player, String message) {
        Component component = AdventureUtils.toComponent(message);
        player.sendActionBar(component);
    }

    public static void sendAll(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(AdventureUtils.toComponent(message));
        }
    }

    public static void send(@NotNull Player player, String message, Enums.MessageType type) {
        if (player.isOnline()) {
            player.sendActionBar(AdventureUtils.toComponent(MessageUtils.getColor(type) + message));
        }
    }



    public static void sendAll(String message, Enums.MessageType type) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(AdventureUtils.toComponent(MessageUtils.getColor(type) + message));
        }
    }
}