package net.cozyvanilla.cozylib.modules.util;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.common.annotations.NotThreadSafe;
import net.cozyvanilla.cozylib.util.text.TextBuilder;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.util.text.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Console {
    private static final CommandSender console = Bukkit.getConsoleSender();

    private Console() {}

    /**
     * Prints a message with the given prefix and color based on the message type.
     *
     * @param prefix the prefix to display before the message
     * @param message the message to print
     * @param type the message type used to apply a predefined color
     */
    @NotThreadSafe
    public static void print(String prefix, String message, MessageType type) {
        message = TextBuilder.of(message).color(Config.getColor(type)).string();
        console.sendMessage(AdventureUtils.toComponent(prefix + " " + message));
    }

    /**
     * Prints a message with the default library prefix and color based on the message type.
     *
     * @param message the message to print
     * @param type the message type used to apply a predefined color
     */
    @NotThreadSafe
    public static void print(String message, MessageType type) {
        message = TextBuilder.of(message).color(Config.getColor(type)).string();
        console.sendMessage(AdventureUtils.toComponent(Config.getPrefix() + " " + message));
    }

    /**
     * Prints a MiniMessage-formatted message with the given prefix.
     *
     * @param prefix the prefix to display before the message
     * @param message the message to print
     */
    @NotThreadSafe
    public static void print(String prefix, String message) {
        console.sendMessage(AdventureUtils.toComponent(prefix + " " + message));
    }

    /**
     * Prints a MiniMessage-formatted message with the default library prefix.
     *
     * @param message the message to print
     */
    @NotThreadSafe
    public static void print(String message) {
        console.sendMessage(AdventureUtils.toComponent(Config.getPrefix() + " " + message));
    }

    /**
     * Pretty prints a message with the given prefix and applies hex or gradient color formatting.
     * Supports formats like "#FF0000" for solid color or "#FF0000:#FFFFFF" for gradient.
     *
     * @param prefix the prefix to display before the message
     * @param message the message to print
     * @param colors the hex color or gradient string to apply
     */
    @NotThreadSafe
    public static void pPrint(String prefix, String message, String colors) {
        message = TextBuilder.of(message).color(colors).string();
        console.sendMessage(AdventureUtils.toComponent(prefix + " " + message));
    }

    /**
     * Pretty prints a message with the default library prefix and applies hex or gradient color formatting.
     * Supports formats like "#FF0000" for solid color or "#FF0000:#FFFFFF" for gradient.
     *
     * @param message the message to print
     * @param colors the hex color or gradient string to apply
     */
    @NotThreadSafe
    public static void pPrint(String message, String colors) {
        message = TextBuilder.of(message).color(colors).string();
        console.sendMessage(AdventureUtils.toComponent(Config.getPrefix() + " " + message));
    }

    /**
     * Sends a TextBuilder message to the console.
     *
     * @param text the text builder to send
     */
    @NotThreadSafe
    public static void print(TextBuilder text) {
        console.sendMessage(AdventureUtils.toComponent(text.string()));
    }

    /**
     * Prints the contents of a map to the console for debugging.
     *
     * @param map the map to print
     * @param <K> the key type
     * @param <V> the value type
     */
    public static <K, V> void map(Map<K, V> map) {
        if (map != null && !map.isEmpty()) {
            print("========================================", MessageType.INFO);
            map.forEach((key, value) -> print("Map debugging: key=" + key + ", value=" + value, MessageType.INFO));
            print("========================================", MessageType.INFO);
        }
    }

    /**
     * Prints the contents of a set to the console for debugging.
     *
     * @param set the set to print
     */
    public static void set(Set<Object> set) {
        if (set != null && !set.isEmpty()) {
            print("========================================", MessageType.INFO);
            set.forEach(object -> print("Set debugging: " + object,  MessageType.INFO));
            print("========================================", MessageType.INFO);
        }
    }

    /**
     * Prints the contents of a list to the console for debugging.
     *
     * @param list the list to print
     */
    public static void list(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            print("========================================", MessageType.INFO);
            list.forEach(value -> print("List debugging: " + value,  MessageType.INFO));
            print("========================================", MessageType.INFO);
        }
    }
}