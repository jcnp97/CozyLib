package asia.virtualmc.cozylib.utilities.bukkit.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtils {

    /**
     * Sends a color-coded message to a specified player based on the message type.
     * <p>
     * The message will include a prefix glyph and color formatting depending on
     * whether the message type is {@code GREEN}, {@code RED}, or {@code YELLOW}.
     * </p>
     *
     * @param player  the player to send the message to (must be online)
     * @param message the message content to send
     * @param type    the message type determining the color and prefix glyph
     */
    public static void sendMessage(@NotNull Player player, String message, EnumsLib.MessageType type) {
        if (player.isOnline()) {
            switch (type) {
                case GREEN -> {
                    player.sendMessage(AdventureUtils.toComponent(GlyphsConfig.get(EnumsLib.Glyphs.MSG_SUCCESS) +
                            " <#6EFFB2>" + message));
                }
                case RED -> {
                    player.sendMessage(AdventureUtils.toComponent(GlyphsConfig.get(EnumsLib.Glyphs.MSG_FAIL) +
                            " <#FF6262>" + message));
                }
                case YELLOW -> {
                    player.sendMessage(AdventureUtils.toComponent(GlyphsConfig.get(EnumsLib.Glyphs.MSG_NOTIFY) +
                            " <#FCD05C>" + message));
                }
            }
        }
    }

    /**
     * Broadcasts a message to all players on the server using a default broadcast format.
     * <p>
     * The broadcast includes a broadcast glyph and a gold gradient color scheme.
     * </p>
     *
     * @param message the message to broadcast to all players
     */
    public static void sendBroadcast(String message) {
        Bukkit.getServer().sendMessage(AdventureUtils.toComponent(GlyphsConfig.get(EnumsLib.Glyphs.MSG_BROADCAST) +
                " <gradient:#ebd197:#b48811>" + message));
    }

    /**
     * Broadcasts a message to all players on the server using a color-coded message type.
     * <p>
     * The broadcast will use a glyph and color corresponding to the specified message type
     * ({@code GREEN}, {@code YELLOW}, or {@code RED}).
     * </p>
     *
     * @param message the message to broadcast to all players
     * @param type    the message type determining the color and prefix glyph
     */
    public static void sendBroadcast(String message, EnumsLib.MessageType type) {
        switch (type) {
            case GREEN -> Bukkit.getServer().sendMessage(AdventureUtils
                    .toComponent(GlyphsConfig.get(EnumsLib.Glyphs.MSG_SUCCESS) + " <#6EFFB2>" + message));
            case YELLOW -> Bukkit.getServer().sendMessage(AdventureUtils
                    .toComponent(GlyphsConfig.get(EnumsLib.Glyphs.MSG_NOTIFY) + " <#FCD05C>" + message));
            case RED -> Bukkit.getServer().sendMessage(AdventureUtils
                    .toComponent(GlyphsConfig.get(EnumsLib.Glyphs.MSG_FAIL) + " <#FF6262>" + message));
        }
    }

    /**
     * Sends an interactive message to a player that, when clicked, suggests a command in chat.
     * <p>
     * The message is formatted in green and can be clicked to auto-fill a specified command
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

    public static String getColor(EnumsLib.MessageType type) {
        return switch (type) {
            case GREEN -> GlyphsConfig.get(EnumsLib.Glyphs.MSG_SUCCESS) + " <#6EFFB2>";
            case YELLOW -> GlyphsConfig.get(EnumsLib.Glyphs.MSG_NOTIFY) + " <#FCD05C>";
            case RED -> GlyphsConfig.get(EnumsLib.Glyphs.MSG_FAIL) + " <#FF6262>";
        };
    }
}