package net.cozyvanilla.cozylib.utilities.paper;

import net.cozyvanilla.cozylib.annotations.NotFoliaSafe;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncUtils {

    public static CompletableFuture<Void> async(Plugin plugin, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        plugin.getServer().getAsyncScheduler().runNow(plugin, task -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public static void asyncAndForget(Plugin plugin, Runnable task) {
        String prefix = "[" + plugin.getName() + "] ";
        plugin.getServer().getAsyncScheduler().runNow(plugin, t -> {
            try {
                task.run();
            } catch (Throwable th) {
                Console.severe(prefix, "Async task failed: " + th.getMessage());
            }
        });
    }

    @NotFoliaSafe
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

            plugin.getServer().getScheduler().runTask(plugin, () -> callback.accept(result));
        });
    }

    public static <T> CompletableFuture<T> supplyAsync(Plugin plugin, Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();

        plugin.getServer().getAsyncScheduler().runNow(plugin, task -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public static CompletableFuture<Void> region(Plugin plugin, Location location, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        plugin.getServer().getRegionScheduler().execute(plugin, location, () -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }
}