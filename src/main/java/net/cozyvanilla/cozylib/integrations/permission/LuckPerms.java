package net.cozyvanilla.cozylib.integrations.permission;

import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.common.enums.PluginState;
import net.cozyvanilla.cozylib.integrations.Integration;
import net.cozyvanilla.cozylib.util.bukkit.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

public final class LuckPerms implements Integration {

    // static
    private static final String pluginName = "LuckPerms";
    private static PluginState state;
    private static File directory;

    private static LuckPermsGet get;

    public LuckPerms() {}

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
    }

    @Override
    public void disable() {
        state = PluginState.INSTALLED_NOT_LOADED;
    }

    @Override
    public void load() {
        if (state == PluginState.LOADED) return;

        RegisteredServiceProvider<net.luckperms.api.LuckPerms> provider = Bukkit.getServicesManager().getRegistration(net.luckperms.api.LuckPerms.class);
        if (provider != null) {
            net.luckperms.api.LuckPerms api = provider.getProvider();

            // load APIs
            get = new LuckPermsGet(api);

            // change into load state
            state = PluginState.LOADED;
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

    public static LuckPermsGet get() {
        require();
        return get;
    }
}
