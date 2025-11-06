package asia.virtualmc.cozylib.configs;

import asia.virtualmc.cozylib.CozyLib;
import asia.virtualmc.cozylib.Enums;
import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;
import asia.virtualmc.cozylib.utilities.files.YamlUtils;
import dev.dejvokep.boostedyaml.YamlDocument;

import java.io.File;
import java.util.*;

public class GlyphsConfig {
    private static final Map<String, String> glyphsCache = new HashMap<>();
    private static final Map<String, List<String>> animationCache = new HashMap<>();

    public void load() {
        try {
            File directory = new File(CozyLib.getInstance().getDataFolder(), "glyphs");
            Map<String, YamlDocument> files = YamlUtils.getFiles(directory, false);
            for (Map.Entry<String, YamlDocument> entry : files.entrySet()) {
                String yamlName = entry.getKey();
                YamlDocument yaml = entry.getValue();
                if (yaml == null) continue;

                Set<String> keys = yaml.getRoutesAsStrings(false);
                for (String key : keys) {
                    Object value = yaml.get(key);

                    if (value instanceof List<?>) {
                        if (animationCache.containsKey(key)) {
                            ConsoleUtils.severe("An animation key duplicate has been found: " + key + " from " + yamlName);
                            continue;
                        }
                        List<String> list = (List<String>) value;
                        animationCache.put(key, list);
                    } else if (value instanceof String) {
                        if (glyphsCache.containsKey(key)) {
                            ConsoleUtils.severe("A glyph key duplicate has been found: " + key + " from " + yamlName);
                            continue;
                        }
                        glyphsCache.put(key, (String) value);
                    }
                }
            }
        } catch (Exception e) {
            ConsoleUtils.severe("An error occurred when trying to read glyphs: " + e);
        }
    }

    public static String get(Enums.Glyphs glyph) {
        return glyphsCache.getOrDefault(glyph.getKey(), "");
    }

    public static String get(String glyph) {
        return glyphsCache.getOrDefault(glyph, "");
    }

    public static List<String> getAnimation(String animName) {
        return animationCache.getOrDefault(animName, new ArrayList<>());
    }

    public static int getCount(String value) {
        return Math.toIntExact(glyphsCache.keySet().stream()
                .filter(k -> k.contains(value))
                .count());
    }
}