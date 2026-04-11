package net.cozyvanilla.cozylib.utilities.paper;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskUtils {

    /**
     * Runs a repeating task on the main thread at a fixed tick interval.
     *
     * @param plugin        the plugin instance
     * @param runnable      task to execute
     * @param intervalTicks interval in ticks
     * @return scheduled task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable runnable, long intervalTicks) {
        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                t -> runnable.run(),
                intervalTicks,
                intervalTicks
        );
    }

    /**
     * Runs a repeating task on the main thread at a fixed second interval.
     *
     * @param plugin          the plugin instance
     * @param runnable        task to execute
     * @param intervalSeconds interval in seconds
     * @return scheduled task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable runnable, double intervalSeconds) {
        return repeating(plugin, runnable, (long) (intervalSeconds * 20));
    }

    /**
     * Runs a repeating task asynchronously at a fixed second interval.
     *
     * @param plugin          the plugin instance
     * @param runnable        task to execute
     * @param intervalSeconds interval in seconds
     * @return scheduled task
     */
    public static ScheduledTask repeatingAsync(Plugin plugin, Runnable runnable, double intervalSeconds) {
        long toLong = (long) (intervalSeconds * 1000);
        return plugin.getServer().getAsyncScheduler().runAtFixedRate(
                plugin,
                t -> runnable.run(),
                toLong,
                toLong,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Runs a repeating task asynchronously at a fixed tick interval.
     *
     * @param plugin        the plugin instance
     * @param runnable      task to execute
     * @param intervalTicks interval in ticks
     * @return scheduled task
     */
    public static ScheduledTask repeatingAsync(Plugin plugin, Runnable runnable, long intervalTicks) {
        return repeatingAsync(plugin, runnable, (double) intervalTicks / 20);
    }

    /**
     * Runs a delayed task on the main thread after a specified tick delay.
     *
     * @param plugin   the plugin instance
     * @param runnable task to execute
     * @param ticks    delay in ticks
     * @return scheduled task
     */
    public static ScheduledTask delayed(Plugin plugin, Runnable runnable, long ticks) {
        return plugin.getServer().getGlobalRegionScheduler().runDelayed(
                plugin,
                t -> runnable.run(),
                ticks
        );
    }

    /**
     * Runs a delayed task on the main thread after a specified second delay.
     *
     * @param plugin   the plugin instance
     * @param runnable task to execute
     * @param seconds  delay in seconds
     * @return scheduled task
     */
    public static ScheduledTask delayed(Plugin plugin, Runnable runnable, double seconds) {
        return delayed(plugin, runnable, (long) (seconds * 20));
    }

    /**
     * Runs a repeating task on the main thread for a limited number of executions.
     *
     * @param plugin        the plugin instance
     * @param runnable      task to execute
     * @param intervalTicks interval in ticks
     * @param count         number of executions before stopping
     * @return scheduled task
     */
    public static ScheduledTask repeatingTemp(Plugin plugin, Runnable runnable, long intervalTicks, int count) {
        AtomicInteger executionCount = new AtomicInteger(0);

        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                scheduledTask -> {
                    if (executionCount.incrementAndGet() >= count) {
                        runnable.run();
                        scheduledTask.cancel();
                    } else {
                        runnable.run();
                    }
                },
                intervalTicks,
                intervalTicks
        );
    }

    /**
     * Runs a repeating task on the main thread for a limited number of executions using seconds.
     *
     * @param plugin          the plugin instance
     * @param runnable        task to execute
     * @param intervalSeconds interval in seconds
     * @param count           number of executions before stopping
     * @return scheduled task
     */
    public static ScheduledTask repeatingTemp(Plugin plugin, Runnable runnable, double intervalSeconds, int count) {
        return repeatingTemp(plugin, runnable, (long) (intervalSeconds * 20), count);
    }
}
