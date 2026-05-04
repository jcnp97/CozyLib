package net.cozyvanilla.cozylib.common.enums;

public enum Glyph {
    MSG_SUCCESS("message_success"),
    MSG_FAIL("message_fail"),
    MSG_BROADCAST("message_broadcast"),
    MSG_NOTIFY("message_notify"),
    ACTION_LEFT_CLICK("action_left_click"),
    ACTION_RIGHT_CLICK("action_right_click");

    private final String key;
    Glyph(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }
}