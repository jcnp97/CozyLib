package asia.virtualmc.cozylib.services.bukkit;

import asia.virtualmc.cozylib.integration.MorePDCUtils;
import asia.virtualmc.cozylib.services.files.YamlFileReader;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemPDCWriter {

    // Todo: Add pdc maps
    public record PDCConfig(
            Map<String, String> pdcString,
            Map<String, Integer> pdcInt,
            Map<String, Double> pdcDouble,
            Map<String, Long> pdcLong,
            Map<String, Set<String>> pdcSet
    ) {}

    /**
     * Creates an ItemStack with the specified PDC configuration applied.
     *
     * @param plugin     the plugin instance used for NamespacedKeys
     * @param item       the target item
     * @param pdcConfig  the configuration containing PDC data
     * @return a new ItemStack with applied PDC data, or null if invalid
     */
    public static ItemStack get(Plugin plugin, ItemStack item, PDCConfig pdcConfig) {
        if (plugin == null || item == null || pdcConfig == null) return null;
        return new PDC(plugin, item, pdcConfig).build();
    }

    /**
     * Reads and parses PDC configuration data from the provided YAML file.
     * Retrieves all item keys and their associated PDC values.
     *
     * @param file the YAML file to read from
     * @return a map of item identifiers to their PDC configurations
     */
    public static Map<String, PDCConfig> getConfig(YamlFileReader.YamlFile file) {
        Map<String, PDCConfig> map = new HashMap<>();
        if (file == null) return map;

        Set<String> keys = file.stringSet("items");
        for (String key : keys) {
            String route = "items." + key + ".pdc-data";

            Map<String, String> pdcString = file.stringKeyStringMap(route + ".string", false);
            Map<String, Integer> pdcInt = file.stringKeyIntMap(route + ".integer", false);
            Map<String, Double> pdcDouble = file.stringKeyDoubleMap(route + ".double", false);
            Map<String, Long> pdcLong = file.stringKeyLongMap(route + ".long", false);
            Map<String, Set<String>> pdcSet = file.stringKeySetMap(route + ".set", false);
            map.put(key, new PDCConfig(pdcString, pdcInt, pdcDouble, pdcLong, pdcSet));
        }

        return map;
    }

    public static class PDC {
        private final Plugin plugin;
        private final ItemStack item;
        private final PDCConfig pdcConfig;
        private final ItemMeta meta;
        private final PersistentDataContainer pdc;

        /**
         * Constructs a PDC handler for the given plugin, item, and configuration.
         *
         * @param plugin     the plugin instance
         * @param item       the base ItemStack to modify
         * @param pdcConfig  the configuration data containing PDC entries
         * @throws IllegalArgumentException if the item type does not support ItemMeta
         */
        public PDC(Plugin plugin, ItemStack item, PDCConfig pdcConfig) {
            this.plugin = plugin;
            this.item = item.clone();
            this.pdcConfig = pdcConfig;
            this.meta = this.item.getItemMeta();
            if (this.meta == null) {
                throw new IllegalArgumentException("Item type does not support ItemMeta: " + item.getType());
            }
            this.pdc = this.meta.getPersistentDataContainer();
        }

        /**
         * Builds and returns the ItemStack with all configured PDC data applied.
         *
         * @return the modified ItemStack with applied PDC values
         */
        public ItemStack build() {
            addString();
            addInteger();
            addDouble();
            addLong();
            addStringSet();

            item.setItemMeta(meta);
            return item;
        }

        /**
         * Adds string-type PDC values to the item.
         */
        private void addString() {
            Map<String, String> map = pdcConfig.pdcString();
            if (map == null || map.isEmpty()) return;

            for (Map.Entry<String, String> entry : map.entrySet()) {
                NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
                pdc.set(key, PersistentDataType.STRING, entry.getValue());
            }
        }

        /**
         * Adds integer-type PDC values to the item.
         */
        private void addInteger() {
            Map<String, Integer> map = pdcConfig.pdcInt();
            if (map == null || map.isEmpty()) return;

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
                pdc.set(key, PersistentDataType.INTEGER, entry.getValue());
            }
        }

        /**
         * Adds double-type PDC values to the item.
         */
        private void addDouble() {
            Map<String, Double> map = pdcConfig.pdcDouble();
            if (map == null || map.isEmpty()) return;

            for (Map.Entry<String, Double> entry : map.entrySet()) {
                NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
                pdc.set(key, PersistentDataType.DOUBLE, entry.getValue());
            }
        }

        /**
         * Adds long-type PDC values to the item.
         */
        private void addLong() {
            Map<String, Long> map = pdcConfig.pdcLong();
            if (map == null || map.isEmpty()) return;

            for (Map.Entry<String, Long> entry : map.entrySet()) {
                NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
                pdc.set(key, PersistentDataType.LONG, entry.getValue());
            }
        }

        /**
         * Adds string set-type PDC values to the item using {@link MorePDCUtils}.
         */
        private void addStringSet() {
            Map<String, Set<String>> map = pdcConfig.pdcSet();
            if (map == null || map.isEmpty()) return;

            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                NamespacedKey key = new NamespacedKey(plugin, entry.getKey());
                MorePDCUtils.addStringSet(meta, key, entry.getValue());
            }
        }
    }
}
