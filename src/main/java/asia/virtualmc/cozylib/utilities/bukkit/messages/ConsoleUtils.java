package asia.virtualmc.cozylib.utilities.bukkit.messages;

import asia.virtualmc.cozylib.CozyLib;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ConsoleUtils {

    /**
     * Sends a green info-level message to the console using the default plugin prefix.
     *
     * @param message The message to log.
     */
    public static void info(String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent("<green>" + CozyLib.getPrefix() + " " + message));
    }

    /**
     * Sends a yellow warning-level message to the console using the default plugin prefix.
     *
     * @param message The message to log.
     */
    public static void warning(String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent("<yellow>" + CozyLib.getPrefix() + " " + message));
    }

    /**
     * Sends a red severe-level message to the console using the default plugin prefix.
     *
     * @param message The message to log.
     */
    public static void severe(String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent("<red>" + CozyLib.getPrefix() + " " + message));
    }

    /**
     * Sends a green info-level message to the console using a custom plugin prefix.
     *
     * @param prefix The prefix to prepend to the message.
     * @param message      The message to log.
     */
    public static void info(String prefix, String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent("<green>" + prefix + " " + message));
    }

    /**
     * Sends a yellow warning-level message to the console using a custom plugin prefix.
     *
     * @param prefix The prefix to prepend to the message.
     * @param message      The message to log.
     */
    public static void warning(String prefix, String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent("<yellow>" + prefix + " " + message));
    }

    /**
     * Sends a red severe-level message to the console using a custom plugin prefix.
     *
     * @param prefix The prefix to prepend to the message.
     * @param message      The message to log.
     */
    public static void severe(String prefix, String message) {
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(AdventureUtils.toComponent("<red>" + prefix + " " + message));
    }
}