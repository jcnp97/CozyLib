package net.cozyvanilla.cozylib.services.files;

import com.google.gson.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;

public class JsonFileReader<T> {
    private final String prefix;
    private final Gson gson;
    private final T data;
    private final JsonObject root;

    /**
     * Initializes the reader by loading a JSON file and mapping it to the specified class.
     *
     * @param plugin   the plugin instance
     * @param fileName the file name to load
     * @param clazz    the class to deserialize into
     */
    public JsonFileReader(@NotNull Plugin plugin, @NotNull String fileName, @NotNull Class<T> clazz) {
        this.prefix = "[" + plugin.getName() + "]";
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        File file = init(plugin, fileName);
        this.root = load(file);
        this.data = gson.fromJson(root, clazz);
    }

    /**
     * Creates the file if it does not exist and copies default resource if available.
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
     * Loads the root {@link JsonObject} from the file.
     *
     * @param file the file to read
     * @return parsed root object
     */
    private JsonObject load(File file) {
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
     * Retrieves a {@link JsonElement} from the root using a dot-separated path.
     *
     * @param path the path to the element
     * @return found element or null
     */
    @Nullable
    private JsonElement getElement(@NotNull String path) {
        String[] parts = path.split("\\.");
        JsonElement current = root;

        for (String part : parts) {
            if (current == null || !current.isJsonObject()) {
                return null;
            }

            JsonObject object = current.getAsJsonObject();
            if (!object.has(part)) {
                return null;
            }

            current = object.get(part);
        }

        return current;
    }

    /**
     * Gets the deserialized root object.
     *
     * @return mapped data
     */
    public T get() {
        return data;
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
     * Gets the {@link Gson} instance used by this reader.
     *
     * @return gson instance
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Gets a string value from the given path.
     *
     * @param path the path to the value
     * @return string value or null
     */
    @Nullable
    public String getString(@NotNull String path) {
        JsonElement element = getElement(path);
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }

        JsonPrimitive primitive = element.getAsJsonPrimitive();
        return primitive.isString() || primitive.isNumber() || primitive.isBoolean()
                ? primitive.getAsString()
                : null;
    }

    /**
     * Gets an integer value from the given path.
     *
     * @param path the path to the value
     * @return integer value or null
     */
    @Nullable
    public Integer getInt(@NotNull String path) {
        JsonElement element = getElement(path);
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }

        try {
            return element.getAsInt();
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
    @Nullable
    public Double getDouble(@NotNull String path) {
        JsonElement element = getElement(path);
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }

        try {
            return element.getAsDouble();
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
    @Nullable
    public Boolean getBoolean(@NotNull String path) {
        JsonElement element = getElement(path);
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }

        try {
            return element.getAsBoolean();
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
    @Nullable
    public JsonObject getObject(@NotNull String path) {
        JsonElement element = getElement(path);
        return element != null && element.isJsonObject()
                ? element.getAsJsonObject()
                : null;
    }

    /**
     * Gets a {@link JsonArray} from the given path.
     *
     * @param path the path to the array
     * @return JsonArray or null
     */
    @Nullable
    public JsonArray getArray(@NotNull String path) {
        JsonElement element = getElement(path);
        return element != null && element.isJsonArray()
                ? element.getAsJsonArray()
                : null;
    }

    @Nullable
    public <E> E read(@NotNull String path, @NotNull Class<E> clazz) {
        JsonElement element = getElement(path);
        return element != null ? gson.fromJson(element, clazz) : null;
    }
}