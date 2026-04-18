package net.cozyvanilla.cozylib.utilities.paper;

import net.cozyvanilla.cozylib.CozyLib;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncUtils {

    /**
     * Runs a task asynchronously, then executes a callback on the main thread with the result.
     *
     * @param plugin   the plugin that owns the scheduled tasks
     * @param task     the async task that produces a result
     * @param callback the sync callback that receives the task result
     */
    public static <T> void asyncThenSync(Plugin plugin, Supplier<T> task, Consumer<T> callback) {
        String prefix = "[" + plugin.getName() + "] ";
        plugin.getServer().getAsyncScheduler().runNow(plugin, t -> {
            T result;
            try {
                result = task.get();
            } catch (Exception e) {
                Console.severe(prefix, "Async task failed: " + e.getMessage());
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(result));
        });
    }

    /**
     * Runs a task asynchronously.
     *
     * @param plugin the plugin that owns the scheduled task
     * @param task   the task to execute off the main thread
     */
    public static void async(Plugin plugin, Runnable task) {
        String prefix = "[" + plugin.getName() + "] ";
        plugin.getServer().getAsyncScheduler().runNow(plugin, t -> {
            try {
                task.run();
            } catch (Exception e) {
                Console.severe(prefix, "Async task failed: " + e.getMessage());
            }
        });
    }
}