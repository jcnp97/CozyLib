package net.cozyvanilla.cozylib.integrations.discordsrv;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.common.enums.PluginState;
import net.cozyvanilla.cozylib.integrations.Integration;
import net.cozyvanilla.cozylib.util.bukkit.PluginUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;

public final class DiscordSRV implements Integration {

    // static
    private static final String pluginName = "DiscordSRV";
    private static PluginState state;
    private static File directory;

    private static DiscordSRVUtil util;

    public DiscordSRV() {}

    @Override
    public String getName() {
        return pluginName;
    }

    @Override
    public void enable() {
        Plugin plugin = PluginUtils.getPlugin(getName());
        if (plugin == null) {
            Logger.warning(pluginName + " not found! Disabling integration..");
            return;
        }

        state = PluginState.INSTALLED_NOT_LOADED;
        directory = PluginUtils.getDirectory(getName());
        github.scarsz.discordsrv.DiscordSRV.api.subscribe(this);
    }

    @Override
    public void disable() {
        state = PluginState.INSTALLED_NOT_LOADED;
    }

    @Override
    public void load() {
        if (state == PluginState.LOADED) return;

        // load APIs
        util = new DiscordSRVUtil();

        // change into load state
        state = PluginState.LOADED;
    }

    @Subscribe
    public void onDiscordReady(DiscordReadyEvent e) {
        load();
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

    public static DiscordSRVUtil util() {
        require();
        return util;
    }
}
