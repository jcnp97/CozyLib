package net.cozyvanilla.cozylib.core.util;

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
     * Sends a colored message to the console using the color from the given message type.
     *
     * @param message the message to send
     * @param type the message type used for color
     */
    @NotThreadSafe
    public static void print(String message, MessageType type) {
        message = TextBuilder.of(message).color(Config.getColor(type)).string();
        console.sendMessage(AdventureUtils.toComponent(message));
    }

    /**
     * Sends a message to the console with mini-message formatting support.
     *
     * @param message the message to send
     */
    @NotThreadSafe
    public static void print(String message) {
        console.sendMessage(AdventureUtils.toComponent(message));
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
     * Sends a colored message to the console.
     *
     * @param message the message to send
     * @param color the color to apply
     */
    @NotThreadSafe
    public static void print(String message, String color) {
        message = TextBuilder.of(message).color(color).string();
        console.sendMessage(AdventureUtils.toComponent(message));
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