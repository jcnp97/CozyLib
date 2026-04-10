package net.cozyvanilla.cozylib.services.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class YamlFileReader {
    private final String PREFIX;
    private final YamlDocument YAML;

    /**
     * Constructs a YamlFileReader instance.
     * Initializes the YAML file from the plugin's data folder and sets a logging prefix.
     *
     * @param plugin   the plugin instance used to locate resources and data folder
     * @param fileName the name of the YAML file to load
     */
    public YamlFileReader(@NotNull Plugin plugin, @NotNull String fileName) {
        this.PREFIX = "[" + plugin.getName() + "]";
        this.YAML = init(plugin, fileName);
    }

    /**
     * Initializes and loads the YAML file.
     * If a default resource exists inside the plugin jar, it will be used as a template.
     *
     * @param plugin   the plugin instance
     * @param fileName the YAML file name
     * @return the loaded YamlDocument
     * @throws IllegalStateException if loading fails
     */
    private YamlDocument init(Plugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        file.getParentFile().mkdirs();

        try (InputStream defaultFile = plugin.getResource(fileName)) {
            return (defaultFile != null)
                    ? YamlDocument.create(file, defaultFile)
                    : YamlDocument.create(file);
        } catch (IOException e) {
            throw new IllegalStateException("[" + plugin.getName() + "] Failed to load YAML file: " + fileName, e);
        }
    }

    /**
     * Gets the loaded YAML document.
     *
     * @return the YAML document
     */
    public YamlDocument get() { return YAML; }

    /**
     * Retrieves a section from the YAML using the given route.
     *
     * @param route the path to the section
     * @return the Section object
     * @throws IllegalStateException if the section does not exist
     */
    public Section getSection(String route) {
        Section section = YAML.getSection(route);
        if (section == null) {
            throw new IllegalStateException(PREFIX + " Section " + route + " not found from Yaml File " + YAML.getNameAsString());
        }
        return section;
    }

    /**
     * Converts a section into a Map<String, String>.
     * Each key in the section maps to its corresponding string value.
     *
     * @param section the YAML section
     * @return a map of string keys to string values
     */
    public Map<String, String> stringKeyStringMap(Section section) {
        Map<String, String> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                map.put(key, section.getString(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<String, String> from a route.
     *
     * @param route the section path
     * @return a map of string keys to string values
     */
    public Map<String, String> stringKeyStringMap(String route) {
        return stringKeyStringMap(getSection(route));
    }

    /**
     * Converts a section into a Map<String, Integer>.
     *
     * @param section the YAML section
     * @return a map of string keys to integer values
     */
    public Map<String, Integer> stringKeyIntMap(Section section) {
        Map<String, Integer> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                map.put(key, section.getInt(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<String, Integer> from a route.
     *
     * @param route the section path
     * @return a map of string keys to integer values
     */
    public Map<String, Integer> stringKeyIntMap(String route) {
        return stringKeyIntMap(getSection(route));
    }

    /**
     * Converts a section into a Map<String, Double>.
     *
     * @param section the YAML section
     * @return a map of string keys to double values
     */
    public Map<String, Double> stringKeyDoubleMap(Section section) {
        Map<String, Double> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                map.put(key, section.getDouble(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<String, Double> from a route.
     *
     * @param route the section path
     * @return a map of string keys to double values
     */
    public Map<String, Double> stringKeyDoubleMap(String route) {
        return stringKeyDoubleMap(getSection(route));
    }

    /**
     * Converts a section into a Map<String, Boolean>.
     *
     * @param section the YAML section
     * @return a map of string keys to boolean values
     */
    public Map<String, Boolean> stringKeyBooleanMap(Section section) {
        Map<String, Boolean> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                map.put(key, section.getBoolean(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<String, Boolean> from a route.
     *
     * @param route the section path
     * @return a map of string keys to boolean values
     */
    public Map<String, Boolean> stringKeyBooleanMap(String route) {
        return stringKeyBooleanMap(getSection(route));
    }

    /**
     * Converts a section into a Map<String, Long>.
     *
     * @param section the YAML section
     * @return a map of string keys to long values
     */
    public Map<String, Long> stringKeyLongMap(Section section) {
        Map<String, Long> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                map.put(key, section.getLong(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<String, Long> from a route.
     *
     * @param route the section path
     * @return a map of string keys to long values
     */
    public Map<String, Long> stringKeyLongMap(String route) {
        return stringKeyLongMap(getSection(route));
    }

    /**
     * Converts a section into a Map<String, Set<String>>.
     * Each key maps to a Set converted from a string list.
     *
     * @param section the YAML section
     * @return a map of string keys to sets of strings
     */
    public Map<String, Set<String>> stringKeySetMap(Section section) {
        Map<String, Set<String>> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                List<String> list = section.getStringList(key);
                map.put(key, new HashSet<>(list));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<String, Set<String>> from a route.
     *
     * @param route the section path
     * @return a map of string keys to sets of strings
     */
    public Map<String, Set<String>> stringKeySetMap(String route) {
        return stringKeySetMap(getSection(route));
    }

    /**
     * Converts a section into a Map<Integer, String>.
     * String keys are parsed into integers.
     *
     * @param section the YAML section
     * @return a map of integer keys to string values
     */
    public Map<Integer, String> intKeyStringMap(Section section) {
        Map<Integer, String> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                int intKey = IntegerUtils.toInt(key);
                map.put(intKey, section.getString(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<Integer, String> from a route.
     *
     * @param route the section path
     * @return a map of integer keys to string values
     */
    public Map<Integer, String> intKeyStringMap(String route) {
        return intKeyStringMap(getSection(route));
    }

    /**
     * Converts a section into a Map<Integer, Integer>.
     *
     * @param section the YAML section
     * @return a map of integer keys to integer values
     */
    public Map<Integer, Integer> intKeyIntMap(Section section) {
        Map<Integer, Integer> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                int intKey = IntegerUtils.toInt(key);
                map.put(intKey, section.getInt(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<Integer, Integer> from a route.
     *
     * @param route the section path
     * @return a map of integer keys to integer values
     */
    public Map<Integer, Integer> intKeyIntMap(String route) {
        return intKeyIntMap(getSection(route));
    }

    /**
     * Converts a section into a Map<Integer, Double>.
     *
     * @param section the YAML section
     * @return a map of integer keys to double values
     */
    public Map<Integer, Double> intKeyDoubleMap(Section section) {
        Map<Integer, Double> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                int intKey = IntegerUtils.toInt(key);
                map.put(intKey, section.getDouble(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<Integer, Double> from a route.
     *
     * @param route the section path
     * @return a map of integer keys to double values
     */
    public Map<Integer, Double> intKeyDoubleMap(String route) {
        return intKeyDoubleMap(getSection(route));
    }

    /**
     * Converts a section into a Map<Integer, Boolean>.
     *
     * @param section the YAML section
     * @return a map of integer keys to boolean values
     */
    public Map<Integer, Boolean> intKeyBooleanMap(Section section) {
        Map<Integer, Boolean> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                int intKey = IntegerUtils.toInt(key);
                map.put(intKey, section.getBoolean(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<Integer, Boolean> from a route.
     *
     * @param route the section path
     * @return a map of integer keys to boolean values
     */
    public Map<Integer, Boolean> intKeyBooleanMap(String route) {
        return intKeyBooleanMap(getSection(route));
    }

    /**
     * Retrieves all keys from a section as a list of strings.
     *
     * @param section the YAML section
     * @return a list of keys
     */
    public List<String> stringList(Section section) {
        List<String> list = new ArrayList<>();
        if (section == null) return list;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            list.addAll(keys);
        }

        return list;
    }

    /**
     * Convenience method to retrieve a list of keys from a route.
     *
     * @param route the section path
     * @return a list of keys
     */
    public List<String> stringList(String route) {
        return stringList(getSection(route));
    }

    /**
     * Converts a section into a Map<String, List<String>>.
     *
     * @param section the YAML section
     * @return a map of string keys to string lists
     */
    public Map<String, List<String>> stringListMap(Section section) {
        Map<String, List<String>> map = new HashMap<>();
        if (section == null) return map;

        Set<String> keys = section.getRoutesAsStrings(false);
        if (!keys.isEmpty()) {
            for (String key : keys) {
                map.put(key, section.getStringList(key));
            }
        }

        return map;
    }

    /**
     * Convenience method to retrieve a Map<String, List<String>> from a route.
     *
     * @param route the section path
     * @return a map of string keys to string lists
     */
    public Map<String, List<String>> stringListMap(String route) {
        return stringListMap(getSection(route));
    }

    /**
     * Retrieves all keys from a section as a Set.
     *
     * @param section the YAML section
     * @return a set of keys
     */
    public Set<String> stringSet(Section section) {
        return section.getRoutesAsStrings(false);
    }

    /**
     * Convenience method to retrieve a Set of keys from a route.
     *
     * @param route the section path
     * @return a set of keys, or empty set if section is null
     */
    public Set<String> stringSet(String route) {
        Section section = getSection(route);
        if (section == null) {
            return Collections.emptySet();
        }
        return stringSet(section);
    }
}
