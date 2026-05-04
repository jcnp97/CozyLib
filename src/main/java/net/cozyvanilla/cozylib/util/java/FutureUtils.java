package net.cozyvanilla.cozylib.util.java;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FutureUtils {

    private FutureUtils() {}

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
     * Handles an async Optional result by running the present, empty, or error callback.
     *
     * @param future the future containing an Optional result
     * @param ifPresent runs when the Optional has a value
     * @param ifEmpty runs when the Optional is empty
     * @param onError runs when the future completes with an error
     * @param <T> the value type inside the Optional
     */
    public static <T> void handleOptionalAsync(CompletableFuture<Optional<T>> future,
                                               Consumer<T> ifPresent,
                                               Runnable ifEmpty,
                                               Consumer<Throwable> onError) {
        future.thenAccept(opt -> {
            if (opt.isPresent()) {
                ifPresent.accept(opt.get());
            } else if (ifEmpty != null) {
                ifEmpty.run();
            }
        }).exceptionally(ex -> {
            if (onError != null) {
                onError.accept(ex);
            }
            return null;
        });
    }

    /**
     * Handles an async Optional result by running the present or error callback.
     *
     * @param future the future containing an Optional result
     * @param ifPresent runs when the Optional has a value
     * @param onError runs when the future completes with an error
     * @param <T> the value type inside the Optional
     */
    public static <T> void handleOptionalAsync(CompletableFuture<Optional<T>> future,
                                               Consumer<T> ifPresent,
                                               Consumer<Throwable> onError) {
        handleOptionalAsync(future, ifPresent, null, onError);
    }

    /**
     * Handles an async result by running the success or error callback.
     *
     * @param future the future containing the async result
     * @param onSuccess runs when the future completes successfully
     * @param onError runs when the future completes with an error
     * @param <T> the result type
     */
    public static <T> void handleAsync(CompletableFuture<T> future,
                                       Consumer<T> onSuccess,
                                       Consumer<Throwable> onError) {
        future.thenAccept(result -> {
            if (onSuccess != null) {
                onSuccess.accept(result);
            }
        }).exceptionally(ex -> {
            if (onError != null) {
                onError.accept(ex);
            }
            return null;
        });
    }

    /**
     * Handles an async result by running the success callback.
     *
     * @param future the future containing the async result
     * @param onSuccess runs when the future completes successfully
     * @param <T> the result type
     */
    public static <T> void handleAsync(CompletableFuture<T> future, Consumer<T> onSuccess) {
        handleAsync(future, onSuccess, null);
    }
}
