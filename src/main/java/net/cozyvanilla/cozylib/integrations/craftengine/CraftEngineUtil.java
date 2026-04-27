package net.cozyvanilla.cozylib.integrations.craftengine;

import net.momirealms.craftengine.core.util.Color;
import net.momirealms.craftengine.core.util.Key;

public class CraftEngineUtil {

    public CraftEngineUtil() {}

    public Color fromHex(String hex) {
        hex = hex.replace("#", "");
        int decimal = Integer.parseInt(hex, 16);
        int r = (decimal >> 16) & 0xFF;
        int g = (decimal >> 8) & 0xFF;
        int b = decimal & 0xFF;
        return Color.fromDecimal(Color.toDecimal(r, g, b));
    }

    public Color fromRGB(String rgb) {
        String[] colors = rgb.split(",");
        return Color.fromStrings(colors);
    }

    public Color fromRGB(int r, int g, int b) {
        return Color.fromDecimal(Color.toDecimal(r, g, b));
    }

    public Key toKey(String namespace, String itemName) {
        return new Key(namespace, itemName);
    }

    public Key toKey(String id) {
        String[] parts = id.split(":");
        switch (parts.length) {
            case 1 -> {
                return new Key("craftengine", parts[0]);
            }
            case 2 -> {
                return new Key(parts[0], parts[1]);
            }
            default -> throw new IllegalArgumentException("Invalid CraftEngineItem id '" + id + "'");
        }
    }
}