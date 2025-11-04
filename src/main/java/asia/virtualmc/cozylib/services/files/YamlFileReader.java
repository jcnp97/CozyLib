package asia.virtualmc.cozylib.services.files;

import asia.virtualmc.cozylib.utilities.annotations.Internal;
import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;
import asia.virtualmc.cozylib.utilities.digits.IntegerUtils;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class YamlFileReader {

    /**
     * Represents a loaded YAML file and provides convenience methods to extract mapped data.
     */
    public static class YamlFile {
        private final String prefix;
        private final YamlDocument yaml;

        /**
         * Creates a new YamlFile instance for the given plugin and file name.
         *
         * @param plugin   the plugin instance
         * @param fileName the name of the YAML file to load
         */
        public YamlFile(@NotNull Plugin plugin, @NotNull String fileName) {
            this.prefix = "[" + plugin.getName() + "]";
            this.yaml = loadYaml(plugin, fileName);
        }

        /**
         * Gets the loaded YAML document.
         *
         * @return the YAML document
         */
        public YamlDocument getYaml() {
            return yaml;
        }

        /**
         * Retrieves a section from the YAML file.
         *
         * @param route the section path
         * @return the section
         * @throws IllegalStateException if the section is not found
         */
        public Section getSection(String route) {
            Section section = yaml.getSection(route);
            if (section == null) {
                throw new IllegalStateException(prefix + " Section " + route + " not found from Yaml File " + yaml.getNameAsString());
            }
            return section;
        }

        /**
         * Returns a map of string keys and string values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<String, String> stringKeyStringMap(Section section, boolean debug) {
            Map<String, String> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    map.put(key, section.getString(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of string keys and string values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<String, String> stringKeyStringMap(String route, boolean debug) {
            return stringKeyStringMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of string keys and integer values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<String, Integer> stringKeyIntMap(Section section, boolean debug) {
            Map<String, Integer> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    map.put(key, section.getInt(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of string keys and integer values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<String, Integer> stringKeyIntMap(String route, boolean debug) {
            return stringKeyIntMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of string keys and double values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<String, Double> stringKeyDoubleMap(Section section, boolean debug) {
            Map<String, Double> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    map.put(key, section.getDouble(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of string keys and double values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<String, Double> stringKeyDoubleMap(String route, boolean debug) {
            return stringKeyDoubleMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of string keys and boolean values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<String, Boolean> stringKeyBooleanMap(Section section, boolean debug) {
            Map<String, Boolean> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    map.put(key, section.getBoolean(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of string keys and boolean values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<String, Boolean> stringKeyBooleanMap(String route, boolean debug) {
            return stringKeyBooleanMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of string keys and long values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return a map containing string keys and long values
         */
        public Map<String, Long> stringKeyLongMap(Section section, boolean debug) {
            Map<String, Long> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    map.put(key, section.getLong(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of string keys and long values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return a map containing string keys and long values
         */
        public Map<String, Long> stringKeyLongMap(String route, boolean debug) {
            return stringKeyLongMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of string keys and string sets from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return a map containing string keys and sets of strings
         */
        public Map<String, Set<String>> stringKeySetMap(Section section, boolean debug) {
            Map<String, Set<String>> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    List<String> list = section.getStringList(key);
                    map.put(key, new HashSet<>(list));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of string keys and string sets from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return a map containing string keys and sets of strings
         */
        public Map<String, Set<String>> stringKeySetMap(String route, boolean debug) {
            return stringKeySetMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of integer keys and string values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, String> intKeyStringMap(Section section, boolean debug) {
            Map<Integer, String> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    int intKey = IntegerUtils.toInt(key);
                    map.put(intKey, section.getString(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of integer keys and string values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, String> intKeyStringMap(String route, boolean debug) {
            return intKeyStringMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of integer keys and integer values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, Integer> intKeyIntMap(Section section, boolean debug) {
            Map<Integer, Integer> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    int intKey = IntegerUtils.toInt(key);
                    map.put(intKey, section.getInt(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of integer keys and integer values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, Integer> intKeyIntMap(String route, boolean debug) {
            return intKeyIntMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of integer keys and double values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, Double> intKeyDoubleMap(Section section, boolean debug) {
            Map<Integer, Double> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    int intKey = IntegerUtils.toInt(key);
                    map.put(intKey, section.getDouble(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of integer keys and double values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, Double> intKeyDoubleMap(String route, boolean debug) {
            return intKeyDoubleMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of integer keys and boolean values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, Boolean> intKeyBooleanMap(Section section, boolean debug) {
            Map<Integer, Boolean> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    int intKey = IntegerUtils.toInt(key);
                    map.put(intKey, section.getBoolean(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of integer keys and boolean values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<Integer, Boolean> intKeyBooleanMap(String route, boolean debug) {
            return intKeyBooleanMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a list of string keys from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting list
         */
        public List<String> stringList(Section section, boolean debug) {
            List<String> list = new ArrayList<>();
            if (section == null) return list;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                list.addAll(keys);
            }

            if (debug) {
                ConsoleUtils.printList(Collections.singletonList(list));
            }

            return list;
        }

        /**
         * Returns a list of string keys from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting list
         */
        public List<String> stringList(String route, boolean debug) {
            return stringList(yaml.getSection(route), debug);
        }

        /**
         * Returns a map of string keys and string list values from the given section.
         *
         * @param section the section to read
         * @param debug   whether to print debug output
         * @return the resulting map
         */
        public Map<String, List<String>> stringListMap(Section section, boolean debug) {
            Map<String, List<String>> map = new HashMap<>();
            if (section == null) return map;

            Set<String> keys = section.getRoutesAsStrings(false);
            if (!keys.isEmpty()) {
                for (String key : keys) {
                    map.put(key, section.getStringList(key));
                }
            }

            if (debug) {
                ConsoleUtils.printMap(map);
            }

            return map;
        }

        /**
         * Returns a map of string keys and string list values from the section path.
         *
         * @param route the section route
         * @param debug whether to print debug output
         * @return the resulting map
         */
        public Map<String, List<String>> stringListMap(String route, boolean debug) {
            return stringListMap(yaml.getSection(route), debug);
        }

        /**
         * Returns a set of all keys in the given section.
         *
         * @param section the section to read
         * @return the resulting set
         */
        public Set<String> stringSet(Section section) {
            return section.getRoutesAsStrings(false);
        }

        /**
         * Returns a set of all keys in the section path.
         *
         * @param route the section route
         * @return the resulting set
         */
        public Set<String> stringSet(String route) {
            Section section = yaml.getSection(route);
            if (section == null) {
                return Collections.emptySet();
            }
            return stringSet(section);
        }

        /**
         * Gets a string value from the given route.
         *
         * @param route the route path
         * @return the string value
         */
        public String getString(String route) {
            return yaml.getString(route);
        }

        /**
         * Gets an integer value from the given route.
         *
         * @param route the route path
         * @return the integer value
         */
        public int getInt(String route) {
            return yaml.getInt(route);
        }

        /**
         * Gets a double value from the given route.
         *
         * @param route the route path
         * @return the double value
         */
        public double getDouble(String route) {
            return yaml.getDouble(route);
        }

        /**
         * Gets a boolean value from the given route.
         *
         * @param route the route path
         * @return the boolean value
         */
        public boolean getBoolean(String route) {
            return yaml.getBoolean(route);
        }

        public List<String> getStringList(String route) {
            return yaml.getStringList(route);
        }
    }

    /**
     * Loads a YAML file for the given plugin.
     *
     * @param plugin   the plugin instance
     * @param fileName the file name
     * @return a YamlFile instance
     */
    public static YamlFile get(@NotNull Plugin plugin, @NotNull String fileName) {
        return new YamlFile(plugin, fileName);
    }

    /**
     * Loads a YAML document from the plugin's data folder.
     * <p>
     * ⚠ Internal use only — not intended for public API use.
     *
     * @param plugin   the plugin instance
     * @param fileName the file name
     * @return the loaded YamlDocument
     * @throws IllegalStateException if the file cannot be loaded
     */
    @Internal
    private static YamlDocument loadYaml(@NotNull Plugin plugin, @NotNull String fileName) {
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
}