package asia.virtualmc.cozylib.utilities.bukkit.messages;

import asia.virtualmc.cozylib.Config;
import asia.virtualmc.cozylib.Enums;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtils {

    /**
     * Sends a formatted message to a specific player with a prefix based on the message type.
     * <p>
     * The prefix is determined from the {@link Config.MessagePrefixes} configuration
     * using the {@link #getColor(Enums.MessageType)} method. The message is only sent
     * if the player is currently online.
     * </p>
     *
     * @param player  the player to receive the message
     * @param message the message text to send
     * @param type    the type of message (INFO, WARNING, SEVERE, or NOTIFY)
     */
    public static void sendMessage(@NotNull Player player, String message, Enums.MessageType type) {
        if (player.isOnline()) {
            player.sendMessage(AdventureUtils.toComponent(getColor(type) + " " + message));
        }
    }

    /**
     * Broadcasts a message to all players and the console using the default broadcast prefix.
     * <p>
     * This method delegates to {@link #sendBroadcast(String, Enums.MessageType)} with {@code null}
     * as the message type, resulting in the use of the broadcast prefix from configuration.
     * </p>
     *
     * @param message the message text to broadcast
     */
    public static void sendBroadcast(String message) {
        sendBroadcast(message, null);
    }

    /**
     * Broadcasts a message to all players and the console with an optional message type prefix.
     * <p>
     * If a message type is provided, the prefix is determined from
     * {@link #getColor(Enums.MessageType)}. If {@code type} is {@code null},
     * the broadcast prefix defined in {@link Config.MessagePrefixes} is used instead.
     * </p>
     *
     * @param message the message text to broadcast
     * @param type    the message type to determine the prefix, or {@code null} to use the default broadcast prefix
     */
    public static void sendBroadcast(String message, Enums.MessageType type) {
        if (type != null) {
            Bukkit.getServer().sendMessage(AdventureUtils.toComponent(getColor(type) + " " + message));
        } else {
            Bukkit.getServer().sendMessage(AdventureUtils.toComponent(Config.getMessagePrefixes().broadcast() +
                    " " + message));
        }
    }

    /**
     * Sends an interactive message to a player that, when clicked, suggests a command in chat.
     * <p>
     * The message is formatted in green and can be clicked to automatically fill a specified command
     * into the player’s chat input.
     * </p>
     *
     * @param player  the player to send the clickable message to
     * @param message the message text to display
     * @param command the command to suggest when the message is clicked
     */
    public static void sendCommand(Player player, String message, String command) {
        Component first = Component.text("<#6EFFB2>" + message)
                .clickEvent(ClickEvent.suggestCommand(command));
        player.sendMessage(first);
    }

    /**
     * Retrieves the appropriate message prefix color based on the specified message type.
     * <p>
     * The returned value corresponds to one of the configured message prefixes defined
     * in {@link Config.MessagePrefixes}.
     * </p>
     *
     * @param type the type of message (INFO, WARNING, SEVERE, or NOTIFY)
     * @return the formatted prefix string associated with the given message type
     */
    public static String getColor(Enums.MessageType type) {
        Config.MessagePrefixes prefix = Config.getMessagePrefixes();
        return switch (type) {
            case INFO -> prefix.info();
            case WARNING -> prefix.warning();
            case SEVERE -> prefix.severe();
            case NOTIFY -> prefix.notification();
        };
    }
}