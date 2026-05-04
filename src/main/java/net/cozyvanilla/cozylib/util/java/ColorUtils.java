package net.cozyvanilla.cozylib.util.java;

import java.awt.*;

public class ColorUtils {

    private ColorUtils() {}

    /**
     * Converts a hex color string (e.g., "#FFFFFF") into a {@link Color}.
     * @param hex the hex string (with or without #)
     * @return the parsed Color
     * @throws IllegalArgumentException if hex is null or invalid format
     */
    public static Color fromHex(String hex) {
        if (hex == null) throw new IllegalArgumentException("Hex string cannot be null");

        hex = hex.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        if (hex.length() != 6) {
            throw new IllegalArgumentException("Hex string must be 6 characters long");
        }

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        return new Color(r, g, b);
    }

    /**
     * Converts an RGB string (e.g., "122,34,12") into a {@link Color}.
     * @param rgb the RGB string with 3 comma-separated values
     * @return the parsed Color
     * @throws IllegalArgumentException if rgb is null or invalid format
     */
    public static Color fromRGB(String rgb) {
        if (rgb == null) throw new IllegalArgumentException("RGB string cannot be null");

        String[] parts = rgb.split(",");

        if (parts.length != 3) {
            throw new IllegalArgumentException("RGB string must have 3 components");
        }

        try {
            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].trim());
            int b = Integer.parseInt(parts[2].trim());

            return fromRGB(r, g, b);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid RGB values", e);
        }
    }

    /**
     * Creates a {@link Color} from RGB integer values.
     * Values are clamped between 0 and 255.
     * @param r red value
     * @param g green value
     * @param b blue value
     * @return the resulting Color
     */
    public static Color fromRGB(int r, int g, int b) {
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        return new Color(r, g, b);
    }

    /**
     * Converts RGB values into a hex color string (e.g. "#FFFFFF").
     * Returns white if any value is out of range (0-255).
     *
     * @param r red value (0-255)
     * @param g green value (0-255)
     * @param b blue value (0-255)
     * @return the hex color string
     */
    public static String toHexFromRGB(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            return "#FFFFFF";
        }

        return String.format("#%02X%02X%02X", r, g, b);
    }
}