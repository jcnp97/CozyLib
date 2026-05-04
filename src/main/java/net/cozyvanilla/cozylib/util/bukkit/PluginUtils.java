package net.cozyvanilla.cozylib.util.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PluginUtils {

    private PluginUtils() {}

    /**
     * Gets a plugin instance by its name.
     *
     * @param pluginName the name of the plugin
     * @return the Plugin instance, or null if not found
     */
    @Nullable
    public static Plugin getPlugin(String pluginName) {
        return Bukkit.getPluginManager().getPlugin(pluginName);
    }

    /**
     * Checks if a plugin is installed on the server.
     *
     * @param pluginName the name of the plugin
     * @return true if the plugin exists, otherwise false
     */
    public static boolean isInstalled(String pluginName) {
        return getPlugin(pluginName) != null;
    }

    /**
     * Gets the data folder directory of a plugin.
     *
     * @param pluginName the name of the plugin
     * @return the plugin's data folder, or null if plugin is not found
     */
    @Nullable
    public static File getDirectory(String pluginName) {
        Plugin plugin = getPlugin(pluginName);
        if (plugin == null) {
            return null;
        }

        return plugin.getDataFolder();
    }
}