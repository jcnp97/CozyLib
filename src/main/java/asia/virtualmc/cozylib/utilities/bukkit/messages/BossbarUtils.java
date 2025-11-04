package asia.virtualmc.cozylib.utilities.bukkit.messages;

import asia.virtualmc.cozylib.CozyLib;
import asia.virtualmc.cozylib.Enums;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class BossbarUtils {

    /**
     * Creates a new {@link BossBar} with the given message, color, and progress.
     * The message is toComponented using AdventureUtils, and progress is clamped between 0.0 and 1.0.
     *
     * @param message  The text to display in the boss bar.
     * @param color    The custom color to use.
     * @param progress The progress value between 0.0 and 1.0.
     * @return A configured BossBar instance.
     */
    public static BossBar get(String message, Enums.BossBarColors color, float progress) {
        Component component = AdventureUtils.toComponent(message);
        float progressLimit = Math.max(0.0f, Math.min(progress, 1.0f));

        return BossBar.bossBar(
                component,
                progressLimit,
                color.getColor(),
                BossBar.Overlay.PROGRESS
        );
    }

    /**
     * Updates the name and progress of an existing {@link BossBar}.
     * The message is re-Componented and progress clamped between 0.0 and 1.0.
     *
     * @param bossBar  The BossBar to modify.
     * @param message  The new message to display.
     * @param progress The new progress value (clamped between 0.0 and 1.0).
     */
    public static void modify(BossBar bossBar, String message, float progress) {
        Component component = AdventureUtils.toComponent(message);
        float progressLimit = Math.max(0.0f, Math.min(progress, 1.0f));

        bossBar.name(component);
        bossBar.progress(progressLimit);
    }

    /**
     * Shows a {@link BossBar} to the specified player for a set duration (in seconds).
     * Automatically hides the boss bar after the duration expires.
     *
     * @param player   The player to show the boss bar to.
     * @param bossBar  The BossBar instance to display.
     * @param duration The duration in seconds before the boss bar is hidden.
     */
    public static void show(Player player, BossBar bossBar, double duration) {
        player.showBossBar(bossBar);
        player.getServer().getScheduler().runTaskLater(CozyLib.getInstance(), task -> {
            player.hideBossBar(bossBar);
        }, (long) (duration * 20L));
    }
}
