package net.cozyvanilla.cozylib.modules.util;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.util.text.TextBuilder;
import net.cozyvanilla.cozylib.util.bukkit.SoundUtils;
import net.cozyvanilla.cozylib.util.text.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Messages {

    private Messages() {}

    private static String format(String message, MessageType type) {
        return TextBuilder.of(message).type(type).string();
    }

    /**
     * Sends a formatted message to the player with type styling and optional sound.
     *
     * @param player the player to send the message to
     * @param message the message content
     * @param type the message type for styling
     * @param playSound whether to play the associated sound
     */
    public static void message(@NotNull Player player, String message, MessageType type, boolean playSound) {
        if (player.isOnline()) {
            player.sendMessage(AdventureUtils.toComponent(format(message, type)));
            if (playSound) SoundUtils.playTo(player, Config.getSound(type));
        }
    }

    /**
     * Sends a formatted message to the player with type styling and default sound enabled.
     *
     * @param player the player to send the message to
     * @param message the message content
     * @param type the message type for styling
     */
    public static void message(@NotNull Player player, String message, MessageType type) {
        message(player, message, type, true);
    }

    /**
     * Broadcasts a message to all players with default broadcast styling and optional sound.
     *
     * @param message the message content
     * @param playSound whether to play the broadcast sound
     */
    public static void broadcast(String message, boolean playSound) {
        Bukkit.getServer().sendMessage(AdventureUtils.toComponent(format(message, MessageType.BROADCAST)));
        if (playSound) SoundUtils.playToAll(Config.getSound(MessageType.BROADCAST));
    }

    /**
     * Broadcasts a message to all players with default broadcast styling and sound enabled.
     *
     * @param message the message content
     */
    public static void broadcast(String message) {
        broadcast(message, true);
    }

    /**
     * Broadcasts a message to all players with type styling and optional sound.
     *
     * @param message the message content
     * @param type the message type for styling
     * @param playSound whether to play the associated sound
     */
    public static void broadcast(String message, MessageType type, boolean playSound) {
        Bukkit.getServer().sendMessage(AdventureUtils.toComponent(format(message, type)));
        if (playSound) SoundUtils.playToAll(Config.getSound(type));
    }

    /**
     * Broadcasts a message to all players with type styling and default sound enabled.
     *
     * @param message the message content
     * @param type the message type for styling
     */
    public static void broadcast(String message, MessageType type) {
        broadcast(message, type, true);
    }
}