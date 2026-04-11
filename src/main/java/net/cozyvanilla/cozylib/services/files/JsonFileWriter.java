package net.cozyvanilla.cozylib.services.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;

public class JsonFileWriter {
    private final String prefix;
    private final Gson gson;
    private final File file;
    private final JsonObject root;

    /**
     * Initializes the writer by loading a JSON file from the plugin data folder.
     *
     * @param plugin   the plugin instance
     * @param fileName the file name to load
     */
    public JsonFileWriter(@NotNull Plugin plugin, @NotNull String fileName) {
        this.prefix = "[" + plugin.getName() + "]";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.file = init(plugin, fileName);
        this.root = load();
    }

    /**
     * Initializes the writer using an existing file.
     *
     * @param plugin the plugin instance
     * @param file   the file to load
     */
    public JsonFileWriter(@NotNull Plugin plugin, @NotNull File file) {
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
        if (parent != null) {
            parent.mkdirs();
        }

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
     * Writes a value to the given path, optionally overwriting existing values.
     *
     * @param path      the dot-separated path
     * @param value     the value to write
     * @param overwrite whether to overwrite existing value
     * @return true if written, false otherwise
     */
    private boolean write(@NotNull String path, JsonElement value, boolean overwrite) {
        String[] parts = path.split("\\.");
        JsonObject current = root;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            JsonElement child = current.get(part);

            if (child == null || child.isJsonNull()) {
                JsonObject newObject = new JsonObject();
                current.add(part, newObject);
                current = newObject;
                continue;
            }

            if (!child.isJsonObject()) {
                if (!overwrite) {
                    return false;
                }

                JsonObject newObject = new JsonObject();
                current.add(part, newObject);
                current = newObject;
                continue;
            }

            current = child.getAsJsonObject();
        }

        String lastPart = parts[parts.length - 1];
        if (current.has(lastPart) && !overwrite) {
            return false;
        }

        current.add(lastPart, value == null ? JsonNull.INSTANCE : value);
        save();
        return true;
    }

    /**
     * Writes a string value to the given path.
     *
     * @param path      the path to write
     * @param value     the string value
     * @param overwrite whether to overwrite existing value
     * @return true if written, false otherwise
     */
    public boolean writeString(@NotNull String path, String value, boolean overwrite) {
        return write(path, gson.toJsonTree(value), overwrite);
    }

    /**
     * Writes an integer value to the given path.
     *
     * @param path      the path to write
     * @param value     the integer value
     * @param overwrite whether to overwrite existing value
     * @return true if written, false otherwise
     */
    public boolean writeInt(@NotNull String path, int value, boolean overwrite) {
        return write(path, gson.toJsonTree(value), overwrite);
    }

    /**
     * Writes a double value to the given path.
     *
     * @param path      the path to write
     * @param value     the double value
     * @param overwrite whether to overwrite existing value
     * @return true if written, false otherwise
     */
    public boolean writeDouble(@NotNull String path, double value, boolean overwrite) {
        return write(path, gson.toJsonTree(value), overwrite);
    }

    /**
     * Writes a boolean value to the given path.
     *
     * @param path      the path to write
     * @param value     the boolean value
     * @param overwrite whether to overwrite existing value
     * @return true if written, false otherwise
     */
    public boolean writeBoolean(@NotNull String path, boolean value, boolean overwrite) {
        return write(path, gson.toJsonTree(value), overwrite);
    }

    /**
     * Writes an object (serialized as JSON) to the given path.
     *
     * @param path      the path to write
     * @param value     the object value
     * @param overwrite whether to overwrite existing value
     * @return true if written, false otherwise
     */
    public boolean writeObject(@NotNull String path, @NotNull Object value, boolean overwrite) {
        return write(path, gson.toJsonTree(value), overwrite);
    }

    /**
     * Removes a value at the given path.
     *
     * @param path the path to remove
     * @return true if removed, false otherwise
     */
    public boolean remove(@NotNull String path) {
        String[] parts = path.split("\\.");
        JsonObject current = root;

        for (int i = 0; i < parts.length - 1; i++) {
            JsonElement child = current.get(parts[i]);
            if (child == null || !child.isJsonObject()) {
                return false;
            }
            current = child.getAsJsonObject();
        }

        String lastPart = parts[parts.length - 1];
        if (!current.has(lastPart)) {
            return false;
        }

        current.remove(lastPart);
        save();
        return true;
    }

    /**
     * Saves the current JSON state to file.
     */
    public void save() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(root, writer);
        } catch (IOException e) {
            throw new IllegalStateException(prefix + " Failed to save JSON file: " + file.getName(), e);
        }
    }

    /**
     * Gets the root {@link JsonObject}.
     *
     * @return root object
     */
    public JsonObject getRoot() {
        return root;
    }

    /**
     * Gets the {@link Gson} instance used by this writer.
     *
     * @return gson instance
     */
    public Gson getGson() {
        return gson;
    }

    public File getFile() {
        return file;
    }
}