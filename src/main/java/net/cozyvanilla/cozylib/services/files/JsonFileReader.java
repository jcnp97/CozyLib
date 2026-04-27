package net.cozyvanilla.cozylib.services.files;

import com.google.gson.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonFileReader {

    private final String prefix;
    private final Gson gson;
    private final File file;
    private final JsonObject root;

    /**
     * Initializes the reader by loading a JSON file from the plugin data folder.
     *
     * @param plugin   the plugin instance
     * @param fileName the file name to load
     */
    public JsonFileReader(@NotNull Plugin plugin, @NotNull String fileName) {
        this.prefix = "[" + plugin.getName() + "]";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.file = init(plugin, fileName);
        this.root = load();
    }

    /**
     * Initializes the reader using an existing file.
     *
     * @param plugin the plugin instance
     * @param file   the file to load
     */
    public JsonFileReader(@NotNull Plugin plugin, @NotNull File file) {
        this.prefix = "[" + plugin.getName() + "]";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.file = file;
        this.root = load();
    }

    /**
     * Creates the file if missing and copies default resource if available.
     *
     * @param plugin   the plugin instance
     * @param fileName the file name
     * @return initialized file
     */
    private File init(Plugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);

        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try {
            if (!file.exists()) {
                try (InputStream defaultFile = plugin.getResource(fileName)) {
                    if (defaultFile != null) {
                        Files.copy(defaultFile, file.toPath());
                    } else {
                        file.createNewFile();
                        try (Writer writer = new FileWriter(file)) {
                            writer.write("{}");
                        }
                    }
                }
            }
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(prefix + " Failed to load JSON file: " + fileName, e);
        }
    }

    /**
     * Loads and parses the JSON file into a root {@link JsonObject}.
     *
     * @return parsed root object
     */
    private JsonObject load() {
        try (Reader reader = new FileReader(file)) {
            JsonElement element = JsonParser.parseReader(reader);
            return element != null && element.isJsonObject()
                    ? element.getAsJsonObject()
                    : new JsonObject();
        } catch (IOException e) {
            throw new IllegalStateException(prefix + " Failed to parse JSON file: " + file.getName(), e);
        }
    }

    /**
     * Retrieves a {@link JsonElement} using a dot-separated path.
     *
     * @param path the path to the element
     * @return element or null if not found
     */
    @Nullable
    private JsonElement getElement(@NotNull String path) {
        String[] parts = path.split("\\.");
        JsonElement current = root;

        for (String part : parts) {
            if (!current.isJsonObject()) return null;

            JsonObject obj = current.getAsJsonObject();
            if (!obj.has(part)) return null;

            current = obj.get(part);
        }

        return current;
    }

    /**
     * Gets a string value from the given path.
     *
     * @param path the path to the value
     * @return string value or null
     */
    public @Nullable String getString(@NotNull String path) {
        JsonElement e = getElement(path);
        return (e != null && e.isJsonPrimitive()) ? e.getAsString() : null;
    }

    /**
     * Gets an integer value from the given path.
     *
     * @param path the path to the value
     * @return integer value or null
     */
    public @Nullable Integer getInt(@NotNull String path) {
        JsonElement e = getElement(path);
        try {
            return (e != null && e.isJsonPrimitive()) ? e.getAsInt() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Gets a boolean value from the given path.
     *
     * @param path the path to the value
     * @return boolean value or null
     */
    public @Nullable Boolean getBoolean(@NotNull String path) {
        JsonElement e = getElement(path);
        try {
            return (e != null && e.isJsonPrimitive()) ? e.getAsBoolean() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Gets a double value from the given path.
     *
     * @param path the path to the value
     * @return double value or null
     */
    public @Nullable Double getDouble(@NotNull String path) {
        JsonElement e = getElement(path);
        try {
            return (e != null && e.isJsonPrimitive()) ? e.getAsDouble() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Gets a {@link JsonObject} from the given path.
     *
     * @param path the path to the object
     * @return JsonObject or null
     */
    public @Nullable JsonObject getObject(@NotNull String path) {
        JsonElement e = getElement(path);
        return (e != null && e.isJsonObject()) ? e.getAsJsonObject() : null;
    }

    /**
     * Gets a {@link JsonArray} from the given path.
     *
     * @param path the path to the array
     * @return JsonArray or null
     */
    public @Nullable JsonArray getArray(@NotNull String path) {
        JsonElement e = getElement(path);
        return (e != null && e.isJsonArray()) ? e.getAsJsonArray() : null;
    }

    /**
     * Retrieves a map of String keys to Double values from a JSON object at the given path.
     * Only valid numeric primitive values are included; invalid entries are ignored.
     *
     * @param path the path to the JSON object
     * @return a map of parsed double values, or null if the object does not exist
     */
    public @Nullable Map<String, Double> getDoubleMap(@NotNull String path) {
        JsonObject object = getObject(path);
        if (object == null) return null;

        Map<String, Double> values = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            JsonElement value = entry.getValue();

            if (!value.isJsonPrimitive()) continue;
            try {
                values.put(entry.getKey(), value.getAsDouble());
            } catch (Exception ignored) {
            }
        }

        return values;
    }

    /**
     * Checks if the given path exists in the JSON.
     *
     * @param path the path to check
     * @return true if exists, false otherwise
     */
    public boolean contains(@NotNull String path) {
        return getElement(path) != null;
    }

    /**
     * Gets the root {@link JsonObject}.
     *
     * @return root object
     */
    public JsonObject getRoot() {
        return root;
    }

    public File getFile() {
        return file;
    }
}