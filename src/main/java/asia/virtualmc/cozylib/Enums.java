package asia.virtualmc.cozylib;

import net.kyori.adventure.bossbar.BossBar;

public class Enums {
    public enum DaylightType {
        DAY, DUSK, MORNING, NIGHT;
    }

    public enum WeatherType {
        CLEAR, RAIN, THUNDER;
    }

    public enum UpdateType {
        ADD, SUBTRACT, SET;
    }

    // red, orange, yellow, green
    public enum MessageType {
        SEVERE, WARNING, NOTIFY, INFO;
    }

    public enum BossBarColors {
        BLUE(BossBar.Color.BLUE),
        GREEN(BossBar.Color.GREEN),
        PINK(BossBar.Color.PINK),
        RED(BossBar.Color.RED),
        WHITE(BossBar.Color.WHITE),
        YELLOW(BossBar.Color.YELLOW),
        PURPLE(BossBar.Color.PURPLE);

        private final BossBar.Color color;

        BossBarColors(BossBar.Color color) {
            this.color = color;
        }

        public BossBar.Color getColor() {
            return color;
        }
    }

    public enum TimeUnits {
        HOURLY(3600),
        DAILY(86400),
        WEEKLY(604800),
        MONTHLY(2592000);
        private final long seconds;
        TimeUnits(long seconds) {
            this.seconds = seconds;
        }
        public long getSeconds() {
            return seconds;
        }
    }

    public enum RayTraceType {
        ENTITY,
        BLOCK
    }

    public enum Seasons {
        SPRING,
        SUMMER,
        FALL,
        WINTER,
        DISABLED
    }

    public enum Skills {
        FARMING,
        FISHING,
        MINING,
        ARCHAEOLOGY,
        COOKING,
        INVENTION
    }

    public enum Glyphs {
        MSG_SUCCESS("message_success"),
        MSG_FAIL("message_fail"),
        MSG_BROADCAST("message_broadcast"),
        MSG_NOTIFY("message_notify"),
        ACTION_LEFT_CLICK("action_left_click"),
        ACTION_RIGHT_CLICK("action_right_click");

        private final String key;
        Glyphs(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}