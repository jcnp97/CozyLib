package asia.virtualmc.cozylib.utilities.bukkit.messages;

import asia.virtualmc.cozylib.Enums;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class TitleUtils {

    /**
     * Sends a title and subtitle to the specified player using default display timings.
     * Converts both strings to Adventure components and shows the title if the player is online.
     *
     * @param player   The player to receive the title.
     * @param title    The main title text.
     * @param subtitle The subtitle text.
     */
    public static void send(@NotNull Player player, String title, String subtitle) {
        if (player.isOnline()) {
            Title fullTitle = Title.title(AdventureUtils.toComponent(title),
                    AdventureUtils.toComponent(subtitle));
            player.showTitle(fullTitle);
        }
    }

    /**
     * Sends a title and subtitle to the specified player with a custom stay duration.
     * Fade-in and fade-out durations are set to 0.
     * Converts both strings to Adventure components and shows the title if the player is online.
     *
     * @param player   The player to receive the title.
     * @param title    The main title text.
     * @param subtitle The subtitle text.
     * @param duration The duration (in milliseconds) the title should stay visible.
     */
    public static void send(@NotNull Player player, String title, String subtitle, long duration) {
        if (player.isOnline()) {
            Title.Times TITLE_TIMES = Title.Times.times(Duration.ZERO, Duration.ofMillis(duration), Duration.ZERO);
            Title fullTitle = Title.title(AdventureUtils.toComponent(title),
                    AdventureUtils.toComponent(subtitle), TITLE_TIMES);
            player.showTitle(fullTitle);
        }
    }

    /**
     * Sends a title and subtitle to all online players using default display timings.
     *
     * @param title    The main title text.
     * @param subtitle The subtitle text.
     */
    public static void sendAll(String title, String subtitle) {
        Title fullTitle = Title.title(
                AdventureUtils.toComponent(title),
                AdventureUtils.toComponent(subtitle)
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(fullTitle);
        }
    }

    /**
     * Sends a title and subtitle to all online players with a custom stay duration.
     * Fade-in and fade-out durations are set to 0.
     *
     * @param title    The main title text.
     * @param subtitle The subtitle text.
     * @param duration The duration (in milliseconds) the title should stay visible.
     */
    public static void sendAll(String title, String subtitle, long duration) {
        Title.Times times = Title.Times.times(
                Duration.ZERO,
                Duration.ofMillis(duration),
                Duration.ZERO
        );

        Title fullTitle = Title.title(
                AdventureUtils.toComponent(title),
                AdventureUtils.toComponent(subtitle),
                times
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(fullTitle);
        }
    }

    /**
     * Sends a title and subtitle to the player with custom fade-in, stay, and fade-out durations (in seconds).
     *
     * @param player    the player to send the title to
     * @param title     the title text
     * @param subtitle  the subtitle text
     * @param fadeIn    fade-in duration in seconds (e.g., 0.5 = half a second)
     * @param stay      stay duration in seconds
     * @param fadeOut   fade-out duration in seconds
     */
    public static void send(Player player, String title, String subtitle,
                            double fadeIn, double stay, double fadeOut) {
        if (player == null) return;

        Title.Times times = Title.Times.times(
                Duration.ofMillis((long) (fadeIn * 1000)),
                Duration.ofMillis((long) (stay * 1000)),
                Duration.ofMillis((long) (fadeOut * 1000))
        );

        Title adventureTitle = Title.title(
                AdventureUtils.toComponent(title),
                AdventureUtils.toComponent(subtitle),
                times
        );

        player.showTitle(adventureTitle);
    }
}