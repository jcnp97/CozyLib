package net.cozyvanilla.cozylib.util.text;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.util.java.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public final class TextBuilder {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final String content;
    private String prefix = "";
    private String suffix = "";

    private TextBuilder(String content) {
        this.content = content;
    }

    /**
     * Creates a new Text wrapper from the given string.
     *
     * @param s the text content
     * @return a new Text instance
     */
    public static TextBuilder of(@NotNull String s) {
        return new TextBuilder(s);
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
    public TextBuilder type(MessageType type) {
        String color = Config.getColor(type);
        String icon = Config.getIcon(type);

        if (color != null && !color.isBlank()) {
            this.color(color);
        }

        prefix = "<white>" + icon + " " + prefix;
        return this;
    }

    // ------------ decorations ------------

    /**
     * Applies bold formatting to the text.
     *
     * @return this Text instance
     */
    public TextBuilder bold() {
        prefix += "<bold>";
        suffix = "</bold>" + suffix;
        return this;
    }

    /**
     * Applies italic formatting to the text.
     *
     * @return this Text instance
     */
    public TextBuilder italic() {
        prefix += "<italic>";
        suffix = "</italic>" + suffix;
        return this;
    }

    /**
     * Applies underline formatting to the text.
     *
     * @return this Text instance
     */
    public TextBuilder underline() {
        prefix += "<underlined>";
        suffix = "</underlined>" + suffix;
        return this;
    }

    /**
     * Applies strikethrough formatting to the text.
     *
     * @return this Text instance
     */
    public TextBuilder strikethrough() {
        prefix += "<strikethrough>";
        suffix = "</strikethrough>" + suffix;
        return this;
    }

    /**
     * Applies obfuscated formatting to the text.
     *
     * @return this Text instance
     */
    public TextBuilder obfuscated() {
        prefix += "<obfuscated>";
        suffix = "</obfuscated>" + suffix;
        return this;
    }

    /**
     * Applies a MiniMessage reset tag before the text.
     *
     * @return this Text instance
     */
    public TextBuilder reset() {
        prefix += "<reset>";
        return this;
    }

    // ------------ color formatting ------------

    /**
     * Applies either a hex color or a gradient to the text.
     * <p>
     * Accepted formats:
     * <ul>
     *     <li>{@code "#FF0000"} - hex</li>
     *     <li>{@code "FF0000"} - hex</li>
     *     <li>{@code "#FF0000:#77DD77"} - gradient</li>
     *     <li>{@code "FF0000:77DD77"} - gradient</li>
     * </ul>
     *
     * @param colors the hex color or gradient colors
     * @return this TextBuilder instance
     */
    public TextBuilder color(String colors) {
        if (colors == null || colors.isBlank()) {
            Logger.warning("No color provided");
            return this;
        }

        colors = colors.trim();

        if (!colors.contains(":")) {
            if (!colors.startsWith("#")) {
                colors = "#" + colors;
            }

            if (notValid(colors)) {
                Logger.warning("Invalid hex color provided: " + colors);
                return this;
            }

            prefix += "<" + colors + ">";
            return this;
        }

        String[] split = colors.split(":");
        if (split.length == 0) {
            Logger.warning("No colors provided for gradient");
            return this;
        }

        StringBuilder gradient = new StringBuilder("<gradient");
        int validCount = 0;

        for (String hex : split) {
            if (hex == null || hex.isBlank()) {
                Logger.warning("Invalid gradient color provided: " + colors);
                return this;
            }

            hex = hex.trim();

            if (!hex.startsWith("#")) {
                hex = "#" + hex;
            }

            if (notValid(hex)) {
                Logger.warning("Invalid gradient color provided: " + hex);
                return this;
            }

            gradient.append(":").append(hex);
            validCount++;
        }

        if (validCount < 2) {
            Logger.warning("Gradient requires at least two valid colors: " + colors);
            return this;
        }

        gradient.append(">");
        prefix += gradient;
        suffix = "</gradient>" + suffix;

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
    public TextBuilder rgb(int r, int g, int b) { return color(ColorUtils.toHexFromRGB(r, g, b)); }

    /**
     * Applies rainbow formatting to the text.
     *
     * @param reversed whether the rainbow should be reversed
     * @return this Text instance
     */
    public TextBuilder rainbow(boolean reversed) {
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
    public TextBuilder shadow(String hex, float alpha) {
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
    public TextBuilder shadow(String hex) {
        return shadow(hex, 0.25f);
    }

    // ------------ private helpers ------------
    private static boolean notValid(String hex) {
        if (hex == null || !hex.matches("#[0-9a-fA-F]{6}")) {
            Logger.warning("Invalid hex color: " + hex + " (expected format: #000000)");
            return true;
        }

        return false;
    }
}