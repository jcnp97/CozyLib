package net.cozyvanilla.cozylib.utilities.files;

import com.google.gson.*;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        if (!source.getName().toLowerCase().endsWith(".json")) {
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