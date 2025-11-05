package asia.virtualmc.cozylib.utilities.files;

import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.exceptions.ConstructorException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class YamlUtils {

    /**
     * Loads a YAML file from the plugin's data folder.
     * <p>
     * If a default version of the file exists inside the plugin jar, it will be used as a template.
     * </p>
     *
     * @param plugin   the plugin instance
     * @param fileName the name of the YAML file to load (e.g., "config.yml")
     * @return the {@link YamlDocument} if successfully loaded, or {@code null} if an error occurred
     */
    @Nullable
    public static YamlDocument getFile(@NotNull Plugin plugin, @NotNull String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        try {
            InputStream defaultFile = plugin.getResource(fileName);
            YamlDocument config;

            if (defaultFile != null) {
                config = YamlDocument.create(file, defaultFile);
            } else {
                config = YamlDocument.create(file);
            }

            return config;

        } catch (IOException e) {
            ConsoleUtils.severe("An error occurred when trying to read " + fileName);
            e.getCause();
        }

        return null;
    }

    /**
     * Loads a YAML file from a specified directory path.
     * <p>
     * Returns {@code null} if the file does not exist or cannot be read.
     * </p>
     *
     * @param directory the directory containing the file
     * @param filePath  the relative path of the YAML file
     * @return the {@link YamlDocument}, or {@code null} if loading fails
     */
    @Nullable
    public static YamlDocument getFile(@NotNull File directory, @NotNull String filePath) {
        File file = new File(directory, filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try {
            return YamlDocument.create(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all YAML files from the given directory and optionally its subdirectories.
     * <p>
     * Skips empty or comment-only YAML files. Supports both recursive and non-recursive search.
     * </p>
     *
     * @param directory   the root directory to search
     * @param isRecursive whether to include subdirectories in the search
     * @return a map of file names (without extensions) to their loaded {@link YamlDocument} instances
     */
    public static Map<String, YamlDocument> getFiles(@NotNull File directory, boolean isRecursive) {
        Map<String, YamlDocument> files = new HashMap<>();
        if (!directory.exists()) return files;

        File[] list = directory.listFiles();
        if (list == null) return files;

        for (File file : list) {
            if (file.isDirectory()) {
                if (isRecursive) {
                    files.putAll(getFiles(file, true));
                }
                continue;
            }

            if (!file.isFile() || !file.getName().toLowerCase().endsWith(".yml"))
                continue;

            try {
                boolean hasContent = Files.lines(file.toPath())
                        .anyMatch(line -> !line.trim().isEmpty() && !line.trim().startsWith("#"));
                if (!hasContent) {
                    ConsoleUtils.severe("Skipping empty/comment-only YAML: " + file.getPath());
                    continue;
                }

                YamlDocument yaml = YamlDocument.create(file);
                String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
                files.put(name, yaml);

            } catch (IOException | ConstructorException e) {
                ConsoleUtils.severe("Failed to load YAML file " + file.getPath() + ": " + e.getMessage());
            }
        }

        return files;
    }
}
