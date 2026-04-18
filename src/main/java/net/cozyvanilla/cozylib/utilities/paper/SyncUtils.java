package net.cozyvanilla.cozylib.utilities.paper;

import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SyncUtils {

    /**
     * Runs a task on the main thread, then executes a callback asynchronously with the result.
     *
     * @param plugin   the plugin that owns the scheduled tasks
     * @param task     the sync task that produces a result
     * @param callback the async callback that receives the task result
     */
    public static <T> void syncThenAsync(Plugin plugin, Supplier<T> task, Consumer<T> callback) {
        String prefix = "[" + plugin.getName() + "] ";
        Runnable syncPhase = () -> {
            final T result;
            try {
                result = task.get();
            } catch (Exception e) {
                Console.severe(prefix, "Sync task failed: " + e.getMessage());
                return;
            }

            plugin.getServer().getAsyncScheduler().runNow(plugin, scheduledTask -> {
                try {
                    callback.accept(result);
                } catch (Exception e) {
                    Console.severe(prefix, "Async callback failed: " + e.getMessage());
                }
            });
        };

        if (Bukkit.isPrimaryThread()) {
            syncPhase.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, syncPhase);
        }
    }

    /**
     * Runs a task on the main thread.
     *
     * @param plugin   the plugin that owns the scheduled task
     * @param syncTask the task to run on the main thread
     */
    public static void sync(Plugin plugin, Runnable syncTask) {
        String prefix = "[" + plugin.getName() + "] ";
        Runnable wrapped = () -> {
            try {
                syncTask.run();
            } catch (Exception e) {
                Console.severe(prefix, "Sync task failed: " + e.getMessage());
            }
        };

        if (Bukkit.isPrimaryThread()) {
            wrapped.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, wrapped);
        }
    }
}