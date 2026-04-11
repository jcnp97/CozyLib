package net.cozyvanilla.cozylib.modules.messages;

import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.modules.Module;
import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import net.cozyvanilla.cozylib.utilities.bukkit.SoundUtils;
import net.cozyvanilla.cozylib.utilities.messages.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class Messages implements Module<Void> {
    private final Plugin plugin;

    private static Colors colors;
    private static Icons icons;
    private static Sounds sounds;

    private record Colors(String INFO, String WARNING, String SEVERE, String NOTIFICATION, String BROADCAST) {}
    private record Icons(String INFO, String WARNING, String SEVERE, String NOTIFICATION, String BROADCAST) {}
    private record Sounds(String INFO, String WARNING, String SEVERE, String NOTIFICATION, String BROADCAST) {}

    public Messages(Plugin plugin) {
        this.plugin = plugin;
        enable();
    }

    @Override
    public String getName() {
        return "Player/Broadcast Messages";
    }

    @Override
    public String getPrefix() {
        return "[CozyMessages]";
    }

    @Override
    public Void getCommands() {
        return null;
    }

    @Override
    public void enable() {
        YamlFileReader file = new YamlFileReader(plugin, "modules/messages.yml");

        colors = new Colors(
                file.get().getString("color.info"),
                file.get().getString("color.warning"),
                file.get().getString("color.severe"),
                file.get().getString("color.notification"),
                file.get().getString("color.broadcast")
        );

        icons = new Icons(
                file.get().getString("icon.info"),
                file.get().getString("icon.warning"),
                file.get().getString("icon.severe"),
                file.get().getString("icon.notification"),
                file.get().getString("icon.broadcast")
        );

        sounds = new Sounds(
                file.get().getString("sound.info"),
                file.get().getString("sound.warning"),
                file.get().getString("sound.severe"),
                file.get().getString("sound.notification"),
                file.get().getString("sound.broadcast")
        );
    }

    @Override
    public void disable() {}

    private static String getColor(Enums.MessageType type) {
        return switch (type) {
            case INFO -> colors.INFO();
            case WARNING -> colors.WARNING();
            case SEVERE -> colors.SEVERE();
            case NOTIFY -> colors.NOTIFICATION();
        };
    }

    private static String getIcon(Enums.MessageType type) {
        return switch (type) {
            case INFO -> icons.INFO();
            case WARNING -> icons.WARNING();
            case SEVERE -> icons.SEVERE();
            case NOTIFY -> icons.NOTIFICATION();
        };
    }

    private static String getSound(Enums.MessageType type) {
        return switch (type) {
            case INFO -> sounds.INFO();
            case WARNING -> sounds.WARNING();
            case SEVERE -> sounds.SEVERE();
            case NOTIFY -> sounds.NOTIFICATION();
        };
    }

    /**
     * Sends a formatted message to the player with type styling and optional sound.
     *
     * @param player the player to send the message to
     * @param message the message content
     * @param type the message type for styling
     * @param playSound whether to play the associated sound
     */
    public static void message(@NotNull Player player, String message, Enums.MessageType type, boolean playSound) {
        if (player.isOnline()) {
            if (playSound) SoundUtils.playTo(player, getSound(type));
            player.sendMessage(AdventureUtils.toComponent(
                    "<white>" + getIcon(type) + getColor(type) + " " + message));
        }
    }

    /**
     * Sends a formatted message to the player with type styling and default sound enabled.
     *
     * @param player the player to send the message to
     * @param message the message content
     * @param type the message type for styling
     */
    public static void message(@NotNull Player player, String message, Enums.MessageType type) {
        message(player, message, type, true);
    }

    /**
     * Broadcasts a message to all players with default broadcast styling and optional sound.
     *
     * @param message the message content
     * @param playSound whether to play the broadcast sound
     */
    public static void broadcast(String message, boolean playSound) {
        Bukkit.getServer().sendMessage(AdventureUtils.toComponent(
                "<white>" + icons.BROADCAST() + colors.BROADCAST() + " " + message));
        if (playSound) SoundUtils.playToAll(sounds.BROADCAST());
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
    public static void broadcast(String message, Enums.MessageType type, boolean playSound) {
        Bukkit.getServer().sendMessage(AdventureUtils.toComponent(
                "<white>" + getIcon(type) + getColor(type) + " " + message));
        if (playSound) SoundUtils.playToAll(getSound(type));
    }

    /**
     * Broadcasts a message to all players with type styling and default sound enabled.
     *
     * @param message the message content
     * @param type the message type for styling
     */
    public static void broadcast(String message, Enums.MessageType type) {
        broadcast(message, type, true);
    }
}