package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.integrations.Integration;
import net.momirealms.craftengine.bukkit.api.event.CraftEngineReloadEvent;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class CraftEngine implements Listener, Integration {
    private final Plugin plugin;
    private static Enums.PluginState state = Enums.PluginState.NOT_INSTALLED;
    public static final String DEFAULT_NAMESPACE = "craftengine";

    public CraftEngine(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "CraftEngine";
    }

    @Override
    public void enable() {
        Plugin craftEngine = plugin.getServer().getPluginManager().getPlugin("CraftEngine");

        if (craftEngine == null || !craftEngine.isEnabled()) {
            state = Enums.PluginState.NOT_INSTALLED;
            return;
        }

        state = Enums.PluginState.INSTALLED_NOT_READY;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void disable() {
        state = Enums.PluginState.INSTALLED_NOT_READY;
    }

    @EventHandler
    public void onCraftEngineReload(CraftEngineReloadEvent event) {
        if (event.isFirstReload()) {
            state = Enums.PluginState.READY;
        }
    }

    public static boolean isReady() {
        return state == Enums.PluginState.READY;
    }

    public static void requireReady() {
        switch (state) {
            case READY -> {
                return;
            }
            case NOT_INSTALLED -> throw new IllegalStateException(
                    "CraftEngine integration is unavailable because CraftEngine is not installed or not enabled."
            );
            case INSTALLED_NOT_READY -> throw new IllegalStateException(
                    "CraftEngine integration is unavailable because CraftEngine has not finished initializing yet."
            );
        }
    }

    public static Key toKey(String namespace, String itemName) {
        return new Key(namespace, itemName);
    }

    public static Key toKey(String id) {
        String[] parts = id.split(":");
        switch (parts.length) {
            case 1 -> {
                return new Key(DEFAULT_NAMESPACE, parts[0]);
            }
            case 2 -> {
                return new Key(parts[0], parts[1]);
            }
            default -> throw new IllegalArgumentException("Invalid CraftEngineItem id '" + id + "'");
        }
    }

    private CraftEngine() {
        throw new UnsupportedOperationException("Utility class");
    }
}