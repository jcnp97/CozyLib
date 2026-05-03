package net.cozyvanilla.cozylib.utilities.paper;

import net.cozyvanilla.cozylib.annotations.FoliaUnsafe;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncUtils {

    /**
     * Runs a task asynchronously without returning a result or callback.
     * Logs any thrown exception to console.
     *
     * @param plugin the plugin scheduling the task
     * @param task the task to run asynchronously
     */
    public static void asyncAndForget(Plugin plugin, Runnable task) {
        String prefix = "[" + plugin.getName() + "] ";
        plugin.getServer().getAsyncScheduler().runNow(plugin, t -> {
            try {
                task.run();
            } catch (Throwable th) {
                Console.severe(prefix, "Async task failed: " + th.getMessage());
                th.printStackTrace();
            }
        });
    }

    /**
     * Runs a task asynchronously, then executes a callback on the main thread with the result.
     * ⚠️ Warning: This method is not safe for Folia environments.
     *
     * @param plugin the plugin scheduling the task
     * @param task the async supplier task
     * @param callback the sync callback receiving the result
     * @param <T> the result type
     */
    @FoliaUnsafe
    public static <T> void asyncThenSync(Plugin plugin, Supplier<T> task, Consumer<T> callback) {
        String prefix = "[" + plugin.getName() + "] ";
        plugin.getServer().getAsyncScheduler().runNow(plugin, t -> {
            T result;
            try {
                result = task.get();
            } catch (Exception e) {
                Console.severe(prefix, "Async task failed: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            plugin.getServer().getScheduler().runTask(plugin, () -> callback.accept(result));
        });
    }
}