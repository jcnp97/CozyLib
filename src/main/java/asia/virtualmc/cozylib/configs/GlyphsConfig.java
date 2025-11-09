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

    /**
     * Loads all glyph and animation entries from YAML files located in the "glyphs" directory.
     * Clears existing caches before reloading. Logs duplicate keys or load errors to console.
     */
    public void load() {
        try {
            glyphsCache.clear();
            animationCache.clear();

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
            ConsoleUtils.severe("An error occurred when trying to load glyphs: " + e);
        }
    }

    /**
     * Retrieves a glyph value based on the provided {@link Enums.Glyphs} enum key.
     *
     * @param glyph the glyph enum key to look up
     * @return the corresponding glyph string, or an empty string if not found
     */
    public static String get(Enums.Glyphs glyph) {
        return glyphsCache.getOrDefault(glyph.getKey(), "");
    }

    /**
     * Retrieves a glyph value based on the provided string key.
     *
     * @param glyph the glyph key as a string
     * @return the corresponding glyph string, or an empty string if not found
     */
    public static String get(String glyph) {
        return glyphsCache.getOrDefault(glyph, "");
    }

    /**
     * Retrieves a list of animation frames associated with the given animation name.
     *
     * @param animName the name of the animation
     * @return a list of animation frame strings, or an empty list if none found
     */
    public static List<String> getAnimation(String animName) {
        return animationCache.getOrDefault(animName, new ArrayList<>());
    }

    /**
     * Counts the number of glyph keys that contain the specified substring.
     *
     * @param value the substring to match against glyph keys
     * @return the number of matching glyph keys
     */
    public static int getCount(String value) {
        return Math.toIntExact(glyphsCache.keySet().stream()
                .filter(k -> k.contains(value))
                .count());
    }
}