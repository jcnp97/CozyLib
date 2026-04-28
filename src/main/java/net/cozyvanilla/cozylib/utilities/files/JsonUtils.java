package net.cozyvanilla.cozylib.utilities.files;

import com.google.gson.*;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class JsonUtils {

    private JsonUtils() {}

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Copies a JSON file into the target directory using the given target name.
     *
     * @param targetDir target directory to copy into
     * @param source source JSON file to copy
     * @param targetName target file name
     * @param overwrite whether to replace an existing file
     */
    public static void clone(
            @NotNull File targetDir,
            @NotNull File source,
            @NotNull String targetName,
            boolean overwrite) {

        if (!source.exists() || !source.isFile()) {
            throw new IllegalArgumentException("Source is not a file: " + source);
        }

        if (!isJsonFile(source)) {
            throw new IllegalArgumentException("Source must be a .json file: " + source.getName());
        }

        targetDir.mkdirs();
        File target = resolveJson(targetDir, targetName);
        if (target.exists() && !overwrite) {
            return;
        }

        try {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Console.severe("Failed to clone JSON file: " + e.getMessage());
        }
    }

    /**
     * Copies a JSON file into a plugin data folder path using the given file name.
     *
     * @param plugin plugin used to resolve the data folder
     * @param path folder path inside the plugin data folder
     * @param jsonFile source JSON file to copy
     * @param fileName target file name
     * @param overwrite whether to replace an existing file
     */
    public static void clone(@NotNull Plugin plugin, @NotNull String path, File jsonFile, String fileName, boolean overwrite) {
        File parentDirectory = new File(plugin.getDataFolder(), path);
        clone(parentDirectory, jsonFile, fileName, overwrite);
    }

    /**
     * Retrieves a file from the plugin data folder if it is a JSON file.
     *
     * @param plugin plugin used to resolve the data folder
     * @param path relative file path
     * @return the JSON file, or null if not a JSON file
     */
    @Nullable
    public static File getFile(@NotNull Plugin plugin, @NotNull String path) {
        File file = new File(plugin.getDataFolder(), path);
        if (isJsonFile(file)) {
            return file;
        }

        return null;
    }

    /**
     * Checks whether the given file has a .json extension.
     *
     * @param file file to check
     * @return true if the file is a JSON file, false otherwise
     */
    public static boolean isJsonFile(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }

    /**
     * Copies a JSON file and replaces matching string placeholders with numeric values.
     *
     * @param targetDir target directory to copy into
     * @param source source JSON file to copy
     * @param targetName target file name
     * @param replacements placeholder keys mapped to replacement numbers
     */
    public static void cloneAndReplace(
            @NotNull File targetDir,
            @NotNull File source,
            @NotNull String targetName,
            @NotNull Map<String, ? extends Number> replacements) {

        File target = resolveJson(targetDir, targetName);
        clone(targetDir, source, targetName, true);
        rewriteJson(target, replacements);
    }

    /**
     * Copies a JSON file and replaces matching string placeholders with string values.
     *
     * @param targetDir target directory to copy into
     * @param source source JSON file to copy
     * @param targetName target file name
     * @param replacements placeholder keys mapped to replacement strings
     */
    public static void cloneAndReplaceStrings(
            @NotNull File targetDir,
            @NotNull File source,
            @NotNull String targetName,
            @NotNull Map<String, String> replacements) {

        File target = resolveJson(targetDir, targetName);
        clone(targetDir, source, targetName, true);
        rewriteJsonStrings(target, replacements);
    }

    /**
     * Cleans the given JSON file by deleting its contents and recreating it
     * with an empty JSON object.
     *
     * @param jsonFile the JSON file to clean
     * @return the cleaned JSON file
     */
    @NotNull
    public static File clean(@NotNull File jsonFile) {
        if (!jsonFile.exists() || !jsonFile.isFile()) {
            throw new IllegalArgumentException("File does not exist or is not a file: " + jsonFile);
        }

        if (!isJsonFile(jsonFile)) {
            throw new IllegalArgumentException("File must be a .json file: " + jsonFile.getName());
        }

        try {
            Path path = jsonFile.toPath();
            Files.delete(path);

            Files.writeString(
                    path,
                    "{}",
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            );

            return jsonFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to clean JSON file: " + jsonFile, e);
        }
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    /** Ensures the file name ends with {@code .json}. */
    private static File resolveJson(File dir, String name) {
        if (!name.toLowerCase().endsWith(".json")) {
            name += ".json";
        }
        return new File(dir, name);
    }

    /** Reads, mutates numeric placeholders, and writes back a JSON file. */
    private static void rewriteJson(File file, Map<String, ? extends Number> replacements) {
        try {
            String raw = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            JsonElement root = JsonParser.parseString(raw);
            replaceNumericValues(root, replacements);
            String output = new GsonBuilder().setPrettyPrinting().create().toJson(root);
            Files.writeString(file.toPath(), output, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Console.severe("Failed to rewrite JSON file: " + e.getMessage());
        }
    }

    /** Reads, mutates string placeholders, and writes back a JSON file. */
    private static void rewriteJsonStrings(File file, Map<String, String> replacements) {
        try {
            String raw = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            JsonElement root = JsonParser.parseString(raw);
            replaceStringValues(root, replacements);
            String output = new GsonBuilder().setPrettyPrinting().create().toJson(root);
            Files.writeString(file.toPath(), output, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Console.severe("Failed to rewrite JSON file: " + e.getMessage());
        }
    }

    /**
     * Walks the JSON tree and replaces any string primitive whose value is a key
     * in {@code replacements} with the corresponding {@link Number}.
     */
    private static void replaceNumericValues(JsonElement element, Map<String, ? extends Number> replacements) {
        if (element == null || element.isJsonNull()) return;

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                String placeholder = asPlaceholder(entry.getValue());
                if (placeholder != null && replacements.containsKey(placeholder)) {
                    obj.addProperty(entry.getKey(), replacements.get(placeholder));
                } else {
                    replaceNumericValues(entry.getValue(), replacements);
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                String placeholder = asPlaceholder(arr.get(i));
                if (placeholder != null && replacements.containsKey(placeholder)) {
                    arr.set(i, new JsonPrimitive(replacements.get(placeholder)));
                } else {
                    replaceNumericValues(arr.get(i), replacements);
                }
            }
        }
    }

    /** Same walk, but replaces matching string placeholders with other strings. */
    private static void replaceStringValues(JsonElement element, Map<String, String> replacements) {
        if (element == null || element.isJsonNull()) return;

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                String placeholder = asPlaceholder(entry.getValue());
                if (placeholder != null && replacements.containsKey(placeholder)) {
                    obj.addProperty(entry.getKey(), replacements.get(placeholder));
                } else {
                    replaceStringValues(entry.getValue(), replacements);
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                String placeholder = asPlaceholder(arr.get(i));
                if (placeholder != null && replacements.containsKey(placeholder)) {
                    arr.set(i, new JsonPrimitive(replacements.get(placeholder)));
                } else {
                    replaceStringValues(arr.get(i), replacements);
                }
            }
        }
    }

    /**
     * Returns the string content of a JSON primitive if it is a string,
     * otherwise {@code null}. Used to identify placeholder values.
     */
    private static String asPlaceholder(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive p = element.getAsJsonPrimitive();
            if (p.isString()) return p.getAsString();
        }
        return null;
    }
}