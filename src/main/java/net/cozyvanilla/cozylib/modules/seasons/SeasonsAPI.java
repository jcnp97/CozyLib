package net.cozyvanilla.cozylib.modules.seasons;

import net.cozyvanilla.cozylib.Enums;

public class SeasonsAPI {

    public static Enums.Seasons toEnum(String seasonName) {
        return switch (seasonName.toLowerCase()) {
            case "spring"  -> Enums.Seasons.SPRING;
            case "summer"  -> Enums.Seasons.SUMMER;
            case "fall"  -> Enums.Seasons.FALL;
            case "winter"  -> Enums.Seasons.WINTER;
            default -> null;
        };
    }

    public static Enums.Seasons getCurrentSeason() {
        return null;
    }
}