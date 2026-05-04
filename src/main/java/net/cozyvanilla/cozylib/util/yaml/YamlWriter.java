package net.cozyvanilla.cozylib.util.yaml;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.cozyvanilla.cozylib.CozyLib;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlWriter {
    private final String filePath;
    private final YamlDocument yaml;

    /**
     * Creates a YamlWriter using a plugin instance and file path.
     * @param plugin the plugin used to resolve data folder and resources
     * @param filePath the relative path of the YAML file
     */
    public YamlWriter(@NotNull Plugin plugin, @NotNull String filePath) {
        this.filePath = filePath;
        this.yaml = load(plugin);
    }

    /**
     * Creates a YamlWriter using CozyLib instance and file path.
     * @param filePath the relative path of the YAML file
     */
    public YamlWriter(@NotNull String filePath) {
        this.filePath = filePath;
        this.yaml = load(CozyLib.getInstance());
    }

    /**
     * Loads an existing YAML file or creates a new one if not found.
     * @param plugin the plugin used to resolve file location and defaults
     * @return the loaded YamlDocument
     */
    private YamlDocument load(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), filePath);

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (InputStream defaultFile = plugin.getResource(filePath)) {
            return (defaultFile != null)
                    ? YamlDocument.create(file, defaultFile)
                    : YamlDocument.create(file);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load YAML file: " + filePath, e);
        }
    }

    /**
     * Returns the underlying YamlDocument.
     * @return the YAML document
     */
    public YamlDocument get() {
        return yaml;
    }

    /**
     * Saves the YAML document to disk.
     */
    public void save() {
        try {
            yaml.save();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save YAML file: " + filePath, e);
        }
    }

    /**
     * Writes a value to the given route.
     * @param route the YAML path
     * @param value the value to write
     * @param overwrite if false and route exists, does nothing
     */
    public void write(@NotNull String route, Object value, boolean overwrite) {
        validateRoute(route);

        Object existing = yaml.get(route);
        if (existing != null && !overwrite) {
            return;
        }

        yaml.set(route, value);
    }

    /**
     * Replaces the entire section at the given route with the provided map.
     * @param route the YAML path
     * @param map the map to set
     * @param overwrite if false and route exists, does nothing
     */
    public <K, V> void setMap(@NotNull String route, @NotNull Map<K, V> map, boolean overwrite) {
        validateRoute(route);

        if (yaml.get(route) != null && !overwrite) {
            return;
        }

        yaml.set(route, map);
    }

    /**
     * Appends key-value pairs to a section at the given route. Creates the section if missing.
     * @param route the YAML path
     * @param map the map to append
     * @param overwrite if false and route exists, does nothing
     */
    public <K, V> void appendMap(@NotNull String route, @NotNull Map<K, V> map, boolean overwrite) {
        validateRoute(route);

        Object existing = yaml.get(route);

        if (existing != null && !overwrite) {
            return;
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            yaml.set(route + "." + entry.getKey(), entry.getValue());
        }
    }

    /**
     * Replaces the entire list at the given route.
     * @param route the YAML path
     * @param list the list to set
     * @param overwrite if false and route exists, does nothing
     */
    public <T> void setList(@NotNull String route, @NotNull List<T> list, boolean overwrite) {
        validateRoute(route);

        if (yaml.get(route) != null && !overwrite) {
            return;
        }

        yaml.set(route, list);
    }

    /**
     * Appends values to a list at the given route. Creates the list if missing.
     * @param route the YAML path
     * @param values the values to append
     * @param overwrite if false and route exists, does nothing
     */
    @SuppressWarnings("unchecked")
    public <T> void appendList(@NotNull String route, @NotNull List<T> values, boolean overwrite) {
        validateRoute(route);

        Object existing = yaml.get(route);

        if (existing != null && !overwrite) {
            return;
        }

        List<T> list;

        if (existing instanceof List<?>) {
            list = new ArrayList<>((List<T>) existing);
        } else {
            list = new ArrayList<>();
        }

        list.addAll(values);
        yaml.set(route, list);
    }

    // ------------ internal helpers ------------
    private void validateRoute(@NotNull String route) {
        if (route.isEmpty()) {
            throw new IllegalArgumentException("Route cannot be empty");
        }
    }
}