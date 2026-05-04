package net.cozyvanilla.cozylib.util.paper;

import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.common.annotations.NotFoliaSafe;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncUtils {

    private AsyncUtils() {}

    /**
     * Runs a task asynchronously without returning a result or callback.
     * Logs any thrown exception to console.
     *
     * @param plugin the plugin scheduling the task
     * @param task the task to run asynchronously
     */
    public static void asyncAndForget(Plugin plugin, Runnable task) {
        plugin.getServer().getAsyncScheduler().runNow(plugin, t -> {
            try {
                task.run();
            } catch (Throwable e) {
                Logger.severe("Async task failed: ", e);
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
    @NotFoliaSafe
    public static <T> void asyncThenSync(Plugin plugin, Supplier<T> task, Consumer<T> callback) {
        plugin.getServer().getAsyncScheduler().runNow(plugin, t -> {
            T result;
            try {
                result = task.get();
            } catch (Exception e) {
                Logger.severe("Async task failed: ", e);
                return;
            }

            plugin.getServer().getScheduler().runTask(plugin, () -> callback.accept(result));
        });
    }
}