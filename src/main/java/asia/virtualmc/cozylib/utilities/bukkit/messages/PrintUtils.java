package asia.virtualmc.cozylib.utilities.bukkit.messages;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrintUtils {

    /**
     * Logs each entry in the provided map using the {@code info()} method.
     * <p>
     * This method iterates through the map and prints both the key and value
     * for each entry, formatted as {@code key=<key>, value=<value>}.
     * It will only run if the map is not empty.
     * </p>
     *
     * @param map the map to debug and print entries from
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     */
    public static <K, V> void map(Map<K, V> map) {
        if (map != null && !map.isEmpty()) {
            map.forEach((key, value) -> ConsoleUtils.info("Map debugging: key=" + key + ", value=" + value));
        }
    }

    /**
     * Logs each element in the given set for debugging purposes.
     *
     * @param set the set of objects to log
     */
    public static void set(Set<Object> set) {
        if (set != null && !set.isEmpty()) {
            set.forEach(object -> ConsoleUtils.info("Set debugging: " + object));
        }
    }

    /**
     * Logs each element in the given set for debugging purposes.
     *
     * @param list the list of objects to log
     */
    public static void list(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(value -> ConsoleUtils.info("List debugging: " + value));
        }
    }
}