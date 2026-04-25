package net.cozyvanilla.cozylib.integrations.craftengine;

import net.momirealms.craftengine.core.util.Color;

public class CraftEngineColorUtils {

    public static Color fromHex(String hex) {
        hex = hex.replace("#", "");
        int decimal = Integer.parseInt(hex, 16);
        int r = (decimal >> 16) & 0xFF;
        int g = (decimal >> 8) & 0xFF;
        int b = decimal & 0xFF;
        return Color.fromDecimal(Color.toDecimal(r, g, b));
    }

    public static Color fromRGB(String rgb) {
        String[] colors = rgb.split(",");
        return Color.fromStrings(colors);
    }

    public static Color fromRGB(int r, int g, int b) {
        return Color.fromDecimal(Color.toDecimal(r, g, b));
    }
}