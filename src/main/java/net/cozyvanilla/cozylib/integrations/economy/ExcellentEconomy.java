package net.cozyvanilla.cozylib.integrations.economy;

import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.integrations.Integration;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.utilities.bukkit.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

import java.io.File;

public final class ExcellentEconomy implements Integration {

    // static
    private static final String pluginName = "ExcellentEconomy";
    private static Enums.PluginState state;
    private static File directory;

    private static ExcellentEconomyGet get;
    private static ExcellentEconomyDeposit deposit;
    private static ExcellentEconomyWithdraw withdraw;

    public ExcellentEconomy() {}

    @Override
    public String getName() {
        return pluginName;
    }

    @Override
    public void enable() {
        Plugin plugin = PluginUtils.getPlugin(getName());
        if (plugin == null) {
            Console.severe(pluginName + " not found! Disabling integration..");
            return;
        }

        state = Enums.PluginState.INSTALLED_NOT_LOADED;
        directory = PluginUtils.getDirectory(getName());
    }

    @Override
    public void disable() {
        state = Enums.PluginState.INSTALLED_NOT_LOADED;
    }

    @Override
    public void load() {
        if (state == Enums.PluginState.LOADED) return;

        RegisteredServiceProvider<ExcellentEconomyAPI> provider = Bukkit.getServer().getServicesManager().getRegistration(ExcellentEconomyAPI.class);
        if (provider != null) {
            ExcellentEconomyAPI api = provider.getProvider();
            // load APIs
            get = new ExcellentEconomyGet(api);

            // change into load state
            state = Enums.PluginState.LOADED;
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

    public static ExcellentEconomyGet get() {
        require();
        return get;
    }

    public static ExcellentEconomyWithdraw withdraw() {
        require();
        return withdraw;
    }

    public static ExcellentEconomyDeposit deposit() {
        require();
        return deposit;
    }
}
