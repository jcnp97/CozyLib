package net.cozyvanilla.cozylib.utilities.paper;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FutureUtils {

    /**
     * Runs a task asynchronously using Paper's async scheduler.
     *
     * @param plugin the plugin scheduling the task
     * @param runnable the task to run
     * @return a future completed when the task finishes
     */
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

    /**
     * Runs a task on the region thread for the given location.
     *
     * @param plugin the plugin scheduling the task
     * @param location the location used to choose the region thread
     * @param runnable the task to run
     * @return a future completed when the task finishes
     */
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

    /**
     * Runs a supplier asynchronously using Paper's async scheduler.
     *
     * @param plugin the plugin scheduling the task
     * @param supplier the supplier to run
     * @param <T> the supplied value type
     * @return a future containing the supplied value
     */
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

    /**
     * Handles an optional future result with present, empty, and error callbacks.
     *
     * @param future the optional future to handle
     * @param ifPresent called when the optional has a value
     * @param ifEmpty called when the optional is empty
     * @param onError called when the future fails
     * @param <T> the optional value type
     */
    public static <T> void handleAsync(CompletableFuture<Optional<T>> future,
                                       Consumer<T> ifPresent,
                                       Runnable ifEmpty,
                                       Consumer<Throwable> onError) {
        future.thenAccept(opt ->
                opt.ifPresentOrElse(ifPresent, ifEmpty)
        ).exceptionally(ex -> {
            onError.accept(ex);
            return null;
        });
    }

    /**
     * Handles an optional future result with present and error callbacks.
     *
     * @param future the optional future to handle
     * @param ifPresent called when the optional has a value
     * @param onError called when the future fails
     * @param <T> the optional value type
     */
    public static <T> void handleAsync(CompletableFuture<Optional<T>> future,
                                       Consumer<T> ifPresent,
                                       Consumer<Throwable> onError) {
        future.thenAccept(opt -> opt.ifPresent(ifPresent))
                .exceptionally(ex -> {
                    onError.accept(ex);
                    return null;
                });
    }
}
