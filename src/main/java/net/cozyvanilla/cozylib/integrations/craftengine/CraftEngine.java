package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.CozyLib;
import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.integrations.Integration;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.utilities.bukkit.PluginUtils;
import net.momirealms.craftengine.bukkit.api.event.CraftEngineReloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class CraftEngine implements Listener, Integration {

    // static
    private static final String pluginName = "CraftEngine";
    private static Enums.PluginState state;
    private static File directory;

    private static CraftEngineFurniture furniture;
    private static CraftEngineItem item;
    private static CraftEngineUtil util;

    public CraftEngine() {}

    @Override
    public String getName() {
        return pluginName;
    }

    @Override
    public void enable() {
        Plugin craftEngine = PluginUtils.getPlugin(getName());
        if (craftEngine == null) {
            Logger.warning(pluginName + " not found! Disabling integration..");
            return;
        }

        state = Enums.PluginState.INSTALLED_NOT_LOADED;
        directory = PluginUtils.getDirectory(getName());
        CozyLib.getInstance().getServer().getPluginManager().registerEvents(this, CozyLib.getInstance());
    }

    @Override
    public void disable() {
        state = Enums.PluginState.INSTALLED_NOT_LOADED;
    }

    @Override
    public void load() {
        if (state == Enums.PluginState.LOADED) return;

        // load APIs
        furniture = new CraftEngineFurniture();
        item = new CraftEngineItem();
        util = new CraftEngineUtil();

        // change into load state
        state = Enums.PluginState.LOADED;
    }

    @EventHandler
    public void onCraftEngineReload(CraftEngineReloadEvent e) {
        if (e.isFirstReload()) {
            load();
        }
    }

    private static void require() {
        switch (state) {
            case LOADED -> {
                return;
            }
            case NOT_INSTALLED -> throw new IllegalStateException(
                    pluginName + " integration is unavailable because it is not installed or not enabled."
            );
            case INSTALLED_NOT_LOADED -> throw new IllegalStateException(
                    pluginName + " integration is unavailable because it has not finished initializing yet."
            );
        }
    }

    // public
    public static File getDirectory() {
        return directory;
    }

    public static CraftEngineFurniture furniture() {
        require();
        return furniture;
    }

    public static CraftEngineItem item() {
        require();
        return item;
    }

    public static CraftEngineUtil util() {
        require();
        return util;
    }
}