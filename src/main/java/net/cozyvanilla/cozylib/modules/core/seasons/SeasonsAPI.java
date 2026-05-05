package net.cozyvanilla.cozylib.modules.core.seasons;

import net.cozyvanilla.cozylib.common.enums.SeasonType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SeasonsAPI {
    private static Seasons instance;

    private SeasonsAPI() {}

    static void register(@NotNull Seasons seasons) {
        instance = seasons;
    }

    static void unregister(@NotNull Seasons seasons) {
        instance = null;
    }

    private static @NotNull Seasons require() {
        if (instance == null) {
            throw new IllegalStateException("Seasons module is not initialized");
        }
        return instance;
    }

    public static SeasonType getSeason() {
        return require().getCurrentSeason();
    }

    public static void setSeason(SeasonType season) {
        require().set(season);
    }

    public static void resetSeasonCycle() {
        require().reset();
    }

    public static void sendSeasonMessage(Player player) {
        require().message(player);
    }

    public static boolean isEnabled() {
        return instance != null;
    }

    // ALWAYS ENABLED
    public static SeasonType toEnum(String seasonName) {
        return switch (seasonName.toLowerCase()) {
            case "spring"  -> SeasonType.SPRING;
            case "summer"  -> SeasonType.SUMMER;
            case "fall"  -> SeasonType.FALL;
            case "winter"  -> SeasonType.WINTER;
            default -> null;
        };
    }
}