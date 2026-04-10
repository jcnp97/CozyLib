package net.cozyvanilla.cozylib.modules.messages;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import net.cozyvanilla.cozylib.utilities.messages.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Console {
    private static MessageType messageType;
    public record MessageType(String INFO, String WARNING, String SEVERE) {}

    public Console(Plugin plugin) {
        YamlFileReader file = new YamlFileReader(plugin, "modules/console.yml");
        messageType = new MessageType(
                file.get().getString("color.info"),
                file.get().getString("color.warning"),
                file.get().getString("color.severe")
        );
    }

    /**
     * Sends an info message to the console using the provided prefix.
     *
     * @param prefix the prefix to place before the message
     * @param message the message to send
     */
    public static void info(String prefix, String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent(messageType.INFO() + prefix + " " + message));
    }

    /**
     * Sends an info message to the console using the default config prefix.
     *
     * @param message the message to send
     */
    public static void info(String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent(messageType.INFO() + Config.getPrefix() + " " + message));
    }

    /**
     * Sends a warning message to the console using the provided prefix.
     *
     * @param prefix the prefix to place before the message
     * @param message the message to send
     */
    public static void warning(String prefix, String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent(messageType.WARNING() + prefix + " " + message));
    }

    /**
     * Sends a warning message to the console using the default config prefix.
     *
     * @param message the message to send
     */
    public static void warning(String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent(messageType.WARNING() + Config.getPrefix() + " " + message));
    }

    /**
     * Sends a severe message to the console using the provided prefix.
     *
     * @param prefix the prefix to place before the message
     * @param message the message to send
     */
    public static void severe(String prefix, String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent(messageType.SEVERE() + prefix + " " + message));
    }

    /**
     * Sends a severe message to the console using the default config prefix.
     *
     * @param message the message to send
     */
    public static void severe(String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent(messageType.SEVERE() + Config.getPrefix() + " " + message));
    }

    /**
     * Logs each entry in the provided map using the {@code info()} method.
     * <p>
     * This method iterates through the map and prints both the key and value
     * for each entry, formatted as {@code key=<key>, value=<value>}.
     * It will only run if the map is not empty.
     * </p>
     *
     * @param map the map to debug and print entries from
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     */
    public static <K, V> void map(Map<K, V> map) {
        if (map != null && !map.isEmpty()) {
            map.forEach((key, value) -> info("Map debugging: key=" + key + ", value=" + value));
        }
    }

    /**
     * Logs each element in the given set for debugging purposes.
     *
     * @param set the set of objects to log
     */
    public static void set(Set<Object> set) {
        if (set != null && !set.isEmpty()) {
            set.forEach(object -> info("Set debugging: " + object));
        }
    }

    /**
     * Logs each element in the given set for debugging purposes.
     *
     * @param list the list of objects to log
     */
    public static void list(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(value -> info("List debugging: " + value));
        }
    }
}
