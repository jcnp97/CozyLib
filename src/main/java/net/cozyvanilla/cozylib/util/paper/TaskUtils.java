package net.cozyvanilla.cozylib.util.paper;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.cozyvanilla.cozylib.common.enums.TimeUnit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskUtils {

    /**
     * Runs a repeating synchronous task with delay and interval using a custom TimeUnit.
     *
     * @param plugin   the plugin scheduling the task
     * @param runnable the task to execute
     * @param unit     the time unit for delay and interval
     * @param delay    the initial delay
     * @param interval the repeat interval
     * @return the scheduled task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable runnable, TimeUnit unit, long delay, long interval) {
        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                t -> runnable.run(),
                unit.toTicks(delay),
                unit.toTicks(interval)
        );
    }

    /**
     * Runs a repeating synchronous task with no delay using a custom TimeUnit.
     *
     * @param plugin   the plugin scheduling the task
     * @param runnable the task to execute
     * @param unit     the time unit for interval
     * @param interval the repeat interval
     * @return the scheduled task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable runnable, TimeUnit unit, long interval) {
        return repeating(plugin, runnable, unit, 0L, interval);
    }

    /**
     * Runs a repeating synchronous task with delay and interval in seconds.
     *
     * @param plugin             the plugin scheduling the task
     * @param runnable           the task to execute
     * @param delayInSeconds     the initial delay in seconds
     * @param intervalInSeconds  the repeat interval in seconds
     * @return the scheduled task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable runnable, double delayInSeconds, double intervalInSeconds) {
        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                t -> runnable.run(),
                TimeUnit.SECOND.toTicks(delayInSeconds),
                Math.max(1L, TimeUnit.SECOND.toTicks(intervalInSeconds))
        );
    }

    /**
     * Runs a repeating synchronous task with no delay in seconds.
     *
     * @param plugin            the plugin scheduling the task
     * @param runnable          the task to execute
     * @param intervalInSeconds the repeat interval in seconds
     * @return the scheduled task
     */
    public static ScheduledTask repeating(Plugin plugin, Runnable runnable, double intervalInSeconds) {
        return repeating(plugin, runnable, 0.0, intervalInSeconds);
    }

    /**
     * Runs a repeating asynchronous task with delay and interval using a custom TimeUnit.
     *
     * @param plugin   the plugin scheduling the task
     * @param runnable the task to execute
     * @param unit     the time unit for delay and interval
     * @param delay    the initial delay
     * @param interval the repeat interval
     * @return the scheduled task
     */
    public static ScheduledTask repeatingAsync(Plugin plugin, Runnable runnable, TimeUnit unit, long delay, long interval) {
        return plugin.getServer().getAsyncScheduler().runAtFixedRate(
                plugin,
                t -> runnable.run(),
                unit.toMillis(delay),
                unit.toMillis(interval),
                java.util.concurrent.TimeUnit.MILLISECONDS
        );
    }

    /**
     * Runs a repeating asynchronous task with no delay using a custom TimeUnit.
     *
     * @param plugin   the plugin scheduling the task
     * @param runnable the task to execute
     * @param unit     the time unit for interval
     * @param interval the repeat interval
     * @return the scheduled task
     */
    public static ScheduledTask repeatingAsync(Plugin plugin, Runnable runnable, TimeUnit unit, long interval) {
        return repeatingAsync(plugin, runnable, unit, 0L, interval);
    }

    /**
     * Runs a delayed synchronous task using a custom TimeUnit.
     *
     * @param plugin   the plugin scheduling the task
     * @param runnable the task to execute
     * @param unit     the time unit for delay
     * @param delay    the delay before execution
     * @return the scheduled task
     */
    public static ScheduledTask delayed(Plugin plugin, Runnable runnable, TimeUnit unit, long delay) {
        return plugin.getServer().getGlobalRegionScheduler().runDelayed(
                plugin,
                t -> runnable.run(),
                unit.toTicks(delay)
        );
    }

    /**
     * Runs a delayed synchronous task in seconds.
     *
     * @param plugin         the plugin scheduling the task
     * @param runnable       the task to execute
     * @param delayInSeconds the delay before execution in seconds
     * @return the scheduled task
     */
    public static ScheduledTask delayed(Plugin plugin, Runnable runnable, double delayInSeconds) {
        return plugin.getServer().getGlobalRegionScheduler().runDelayed(
                plugin,
                t -> runnable.run(),
                TimeUnit.SECOND.toTicks(delayInSeconds)
        );
    }

    /**
     * Runs a repeating synchronous task a limited number of times.
     *
     * @param plugin   the plugin scheduling the task
     * @param runnable the task to execute
     * @param unit     the time unit for delay and interval
     * @param delay    the initial delay
     * @param interval the repeat interval
     * @param count    the number of executions before stopping
     * @return the scheduled task
     */
    public static ScheduledTask repeatingTemp(Plugin plugin, Runnable runnable, TimeUnit unit, long delay, long interval, int count) {
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
                unit.toTicks(delay),
                unit.toTicks(interval)
        );
    }

    /**
     * Runs a repeating synchronous task a limited number of times with no delay.
     *
     * @param plugin   the plugin scheduling the task
     * @param runnable the task to execute
     * @param unit     the time unit for interval
     * @param interval the repeat interval
     * @param count    the number of executions before stopping
     * @return the scheduled task
     */
    public static ScheduledTask repeatingTemp(Plugin plugin, Runnable runnable, TimeUnit unit, long interval, int count) {
        return repeatingTemp(plugin, runnable, unit, 0L, interval, count);
    }

    /**
     * Runs a repeating synchronous task a limited number of times using seconds.
     *
     * @param plugin             the plugin scheduling the task
     * @param runnable           the task to execute
     * @param delayInSeconds     the initial delay in seconds
     * @param intervalInSeconds  the repeat interval in seconds
     * @param count              the number of executions before stopping
     * @return the scheduled task
     */
    public static ScheduledTask repeatingTemp(Plugin plugin, Runnable runnable, double delayInSeconds, double intervalInSeconds, int count) {
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
                TimeUnit.SECOND.toTicks(delayInSeconds),
                TimeUnit.SECOND.toTicks(intervalInSeconds)
        );
    }

    /**
     * Runs a repeating synchronous task a limited number of times with no delay in seconds.
     *
     * @param plugin            the plugin scheduling the task
     * @param runnable          the task to execute
     * @param intervalInSeconds the repeat interval in seconds
     * @param count             the number of executions before stopping
     * @return the scheduled task
     */
    public static ScheduledTask repeatingTemp(Plugin plugin, Runnable runnable, double intervalInSeconds, int count) {
        return repeatingTemp(plugin, runnable, 0.0, intervalInSeconds, count);
    }
}