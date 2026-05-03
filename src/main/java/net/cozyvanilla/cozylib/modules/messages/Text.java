package net.cozyvanilla.cozylib.modules.messages;

import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.utilities.java.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public final class Text {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final String content;
    private String prefix = "";
    private String suffix = "";

    private Text(String content) {
        this.content = content;
    }

    /**
     * Creates a new Text wrapper from the given string.
     *
     * @param s the text content
     * @return a new Text instance
     */
    public static Text of(@NotNull String s) {
        return new Text(s);
    }

    /**
     * Returns the raw text with all applied prefixes and suffixes.
     *
     * @return the formatted MiniMessage string
     */
    public String string() {
        return prefix + content + suffix;
    }

    /**
     * Converts the formatted text into an Adventure component.
     *
     * @return the deserialized text component
     */
    public Component component() {
        return MINI_MESSAGE.deserialize(string());
    }

    /**
     * Applies a message type prefix such as info, warning, or severe.
     *
     * @param type the message type to apply
     * @return this Text instance
     */
    public Text type(Enums.MessageType type) {
        prefix = Messages.getMessageType(type) + prefix;
        return this;
    }

    // ------- decorations -------
    /**
     * Applies bold formatting to the text.
     *
     * @return this Text instance
     */
    public Text bold() {
        prefix += "<bold>";
        suffix = "</bold>" + suffix;
        return this;
    }

    /**
     * Applies italic formatting to the text.
     *
     * @return this Text instance
     */
    public Text italic() {
        prefix += "<italic>";
        suffix = "</italic>" + suffix;
        return this;
    }

    /**
     * Applies underline formatting to the text.
     *
     * @return this Text instance
     */
    public Text underline() {
        prefix += "<underlined>";
        suffix = "</underlined>" + suffix;
        return this;
    }

    /**
     * Applies strikethrough formatting to the text.
     *
     * @return this Text instance
     */
    public Text strikethrough() {
        prefix += "<strikethrough>";
        suffix = "</strikethrough>" + suffix;
        return this;
    }

    /**
     * Applies obfuscated formatting to the text.
     *
     * @return this Text instance
     */
    public Text obfuscated() {
        prefix += "<obfuscated>";
        suffix = "</obfuscated>" + suffix;
        return this;
    }

    /**
     * Applies a MiniMessage reset tag before the text.
     *
     * @return this Text instance
     */
    public Text reset() {
        prefix += "<reset>";
        return this;
    }

    // ------- color formatting -------
    /**
     * Applies a hex color to the text.
     *
     * @param hex the hex color in #RRGGBB format
     * @return this Text instance
     */
    public Text hex(String hex) {
        if (notValid(hex)) return this;
        prefix += "<" + hex + ">";
        return this;
    }

    /**
     * Applies an RGB color to the text.
     *
     * @param r the red value
     * @param g the green value
     * @param b the blue value
     * @return this Text instance
     */
    public Text rgb(int r, int g, int b) {
        return hex(ColorUtils.toHexFromRGB(r, g, b));
    }

    /**
     * Applies a gradient using the given hex colors.
     *
     * @param hexColors the hex colors in #RRGGBB format
     * @return this Text instance
     */
    public Text gradient(String... hexColors) {
        if (hexColors == null || hexColors.length == 0) {
            Console.severe("No colors provided for gradient");
            return this;
        }

        StringBuilder gradient = new StringBuilder("<gradient");
        int validCount = 0;

        for (String hex : hexColors) {
            if (!notValid(hex)) {
                gradient.append(":").append(hex);
                validCount++;
            }
        }

        if (validCount == 0) {
            Console.severe("No valid hex colors provided for gradient");
            return this;
        }

        if (validCount == 1) {
            String single = gradient.substring(gradient.lastIndexOf(":") + 1);
            prefix += "<" + single + ">";
            return this;
        }

        gradient.append(">");
        prefix += gradient;
        suffix = "</gradient>" + suffix;
        return this;
    }

    /**
     * Applies rainbow formatting to the text.
     *
     * @param reversed whether the rainbow should be reversed
     * @return this Text instance
     */
    public Text rainbow(boolean reversed) {
        prefix += reversed ? "<rainbow:!>" : "<rainbow>";
        suffix = "</rainbow>" + suffix;
        return this;
    }

    /**
     * Applies shadow formatting to the text.
     *
     * @param hex the shadow hex color in #RRGGBB format
     * @param alpha the shadow opacity from 0 to 1
     * @return this Text instance
     */
    public Text shadow(String hex, float alpha) {
        if (alpha < 0 || alpha > 1) { alpha = 0.25f; }
        if (notValid(hex)) hex = "#FFFFFF";

        prefix += "<shadow:" + hex + ":" + alpha + ">";
        suffix = "</shadow>" + suffix;
        return this;
    }

    /**
     * Applies shadow formatting with the default opacity.
     *
     * @param hex the shadow hex color in #RRGGBB format
     * @return this Text instance
     */
    public Text shadow(String hex) {
        return shadow(hex, 0.25f);
    }

    // ------- private helpers -------
    private static boolean notValid(String hex) {
        if (hex == null || !hex.matches("#[0-9a-fA-F]{6}")) {
            Console.severe("Invalid hex color: " + hex + " (expected format: #000000)");
            return true;
        }

        return false;
    }
}