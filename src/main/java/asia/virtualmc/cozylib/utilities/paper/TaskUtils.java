package asia.virtualmc.cozylib.utilities.paper;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskUtils {

    /**
     * Runs a repeating task using the Paper GlobalRegionScheduler at a fixed interval.
     *
     * @param plugin  the plugin instance running the task
     * @param task    the task to execute
     * @param interval the interval between executions in seconds
     * @return the scheduled repeating task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable task, double interval) {
        long intervalTicks = (long) (interval * 20);

        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                t -> task.run(),
                intervalTicks,
                intervalTicks
        );
    }

    /**
     * Runs a repeating task using the Paper GlobalRegionScheduler at a fixed interval.
     *
     * @param plugin  the plugin instance running the task
     * @param task    the task to execute
     * @param interval the interval between executions in ticks (20 = 1 second)
     * @return the scheduled repeating task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable task, long interval) {
        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                t -> task.run(),
                interval,
                interval
        );
    }

    /**
     * Runs a repeating task using the Paper GlobalRegionScheduler with a fixed interval,
     * and cancels it automatically after the specified duration.
     *
     * @param plugin   the plugin instance running the task
     * @param task     the task to execute
     * @param interval the interval between executions in seconds
     * @param duration the total duration before the task is cancelled, in seconds
     * @return the scheduled repeating task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable task, double interval, double duration) {
        long intervalTicks = (long) (interval * 20);
        long durationTicks = (long) (duration * 20);

        ScheduledTask repeating = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                t -> task.run(),
                intervalTicks,
                intervalTicks
        );

        plugin.getServer().getGlobalRegionScheduler().runDelayed(
                plugin,
                t -> repeating.cancel(),
                durationTicks
        );

        return repeating;
    }

    /**
     * Runs a repeating task asynchronously using the Paper AsyncScheduler at a fixed interval.
     *
     * @param plugin   the plugin instance running the task
     * @param task     the task to execute
     * @param interval the interval between executions in seconds
     * @return the scheduled repeating async task
     */
    public static ScheduledTask repeatingAsync(Plugin plugin, Runnable task, double interval) {
        long intervalMillis = (long) (interval * 1000);

        return plugin.getServer().getAsyncScheduler().runAtFixedRate(
                plugin,
                scheduledTask -> task.run(),
                intervalMillis,
                intervalMillis,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Runs a repeating task using the Paper GlobalRegionScheduler at a fixed interval,
     * and cancels it automatically after executing the specified number of times.
     *
     * @param plugin   the plugin instance running the task
     * @param task     the task to execute
     * @param interval the interval between executions in seconds
     * @param count    the number of times the task should execute before cancelling
     * @return the scheduled repeating task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable task, double interval, int count) {
        long intervalTicks = (long) (interval * 20);
        AtomicInteger executionCount = new AtomicInteger(0);

        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                scheduledTask -> {
                    // increment and run
                    if (executionCount.incrementAndGet() >= count) {
                        task.run();
                        scheduledTask.cancel();
                    } else {
                        task.run();
                    }
                },
                intervalTicks,
                intervalTicks
        );
    }

    /**
     * Runs a repeating task a fixed number of times at a fixed interval.
     *
     * @param plugin         The plugin instance.
     * @param task           The runnable to execute repeatedly.
     * @param intervalTicks  The interval in ticks between executions.
     * @param count          The number of times to execute the task.
     * @return The scheduled task.
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable task, long intervalTicks, int count) {
        return repeating(plugin, task, null, intervalTicks, count);
    }

    /**
     * Runs a repeating task a fixed number of times at a fixed interval,
     * and executes an optional callback when completed.
     *
     * @param plugin         The plugin instance.
     * @param task           The runnable to execute repeatedly.
     * @param intervalTicks  The interval in ticks between executions.
     * @param count          The number of times to execute the task.
     * @param onComplete     The runnable to execute after all iterations are completed.
     * @return The scheduled task.
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable task, Runnable onComplete, long intervalTicks, int count) {
        AtomicInteger executionCount = new AtomicInteger(0);
        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                scheduledTask -> {
                    int current = executionCount.incrementAndGet();

                    try {
                        task.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (current >= count) {
                        scheduledTask.cancel();
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                intervalTicks,
                intervalTicks
        );
    }

    /**
     * Runs a single delayed task using the Paper GlobalRegionScheduler.
     *
     * @param plugin the plugin instance scheduling the task
     * @param task   the task to execute after the delay
     * @param ticks  the delay before execution in ticks (20 = 1 second)
     * @return the scheduled delayed task
     */
    public static ScheduledTask delay(Plugin plugin, Runnable task, long ticks) {
        return plugin.getServer().getGlobalRegionScheduler().runDelayed(
                plugin,
                scheduledTask -> task.run(),
                ticks
        );
    }

    /**
     * Runs a single delayed task using the Paper GlobalRegionScheduler.
     *
     * @param plugin  the plugin instance scheduling the task
     * @param task    the task to execute after the delay
     * @param seconds the delay before execution in seconds
     * @return the scheduled delayed task
     */
    public static ScheduledTask delay(Plugin plugin, Runnable task, double seconds) {
        long ticks = (long) (seconds * 20);
        return plugin.getServer().getGlobalRegionScheduler().runDelayed(
                plugin,
                scheduledTask -> task.run(),
                ticks
        );
    }
}