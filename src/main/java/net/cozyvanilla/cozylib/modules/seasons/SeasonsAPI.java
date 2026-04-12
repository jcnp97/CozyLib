package net.cozyvanilla.cozylib.modules.seasons;

import net.cozyvanilla.cozylib.Enums;
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

    public static Enums.Seasons getSeason() {
        return require().getCurrentSeason();
    }

    public static void setSeason(Enums.Seasons season) {
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
    public static Enums.Seasons toEnum(String seasonName) {
        return switch (seasonName.toLowerCase()) {
            case "spring"  -> Enums.Seasons.SPRING;
            case "summer"  -> Enums.Seasons.SUMMER;
            case "fall"  -> Enums.Seasons.FALL;
            case "winter"  -> Enums.Seasons.WINTER;
            default -> null;
        };
    }
}