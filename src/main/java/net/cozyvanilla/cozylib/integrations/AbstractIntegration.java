package net.cozyvanilla.cozylib.integrations;

import net.cozyvanilla.cozylib.common.enums.PluginState;
import net.cozyvanilla.cozylib.runtime.Config;
import net.cozyvanilla.cozylib.util.bukkit.PluginUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public abstract class AbstractIntegration {
    protected final String name;
    protected File directory;
    protected PluginState state;

    protected AbstractIntegration(@NotNull String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Nullable
    public final String getInitialStatus() {
        state = PluginState.UNINITIALIZED;

        // check if integration is enabled on config.yml
        if (!Config.hasIntegration(name.toLowerCase())) {
            state = PluginState.CONFIG_DISABLED;
            return name + " is set to FALSE from config.yml";
        }

        // check if plugin is installed
        Plugin plugin = PluginUtils.getPlugin(name);
        if (plugin == null) {
            state = PluginState.NOT_INSTALLED;
            return "Plugin is not installed";
        }

        directory = plugin.getDataFolder();
        state = PluginState.LOADING;
        return null;
    }

    public final void disable() { state =  PluginState.DISABLED; }

    public void require() {
        String error = "A class is trying to use a method from " + name + " but integration is ";
        switch (state) {
            case UNINITIALIZED -> throw new IntegrationStateException(error + "not initialized!");
            case CONFIG_DISABLED -> throw new IntegrationStateException(error + "disabled from config.yml!");
            case DISABLED -> throw new IntegrationStateException(error + "is disabled!");
            case NOT_INSTALLED -> throw new IntegrationStateException(error + "not found from the server plugins!");
            case LOADING -> throw new IntegrationStateException(error + "still loading!");
            case FAILED -> throw new IntegrationStateException(error + "failed from hooking into its API!");
            case READY -> {}
        }
    }

    protected final void ready() {
        state = PluginState.READY;
        loadAPIs();
    }

    public abstract void enable();
    protected abstract void loadAPIs();

    public final File getDirectory() {
        return directory;
    }
}