package net.cozyvanilla.cozylib.common.enums;

import net.kyori.adventure.bossbar.BossBar;

public enum BossBarColor {
    BLUE(BossBar.Color.BLUE),
    GREEN(BossBar.Color.GREEN),
    PINK(BossBar.Color.PINK),
    RED(BossBar.Color.RED),
    WHITE(BossBar.Color.WHITE),
    YELLOW(BossBar.Color.YELLOW),
    PURPLE(BossBar.Color.PURPLE);

    private final BossBar.Color color;
    BossBarColor(BossBar.Color color) {
        this.color = color;
    }
    public BossBar.Color getColor() {
        return color;
    }
}