package asia.virtualmc.cozylib;

import org.bukkit.plugin.java.JavaPlugin;

public final class CozyLib extends JavaPlugin {
    private static CozyLib plugin;
    private static final String prefix = "[CozyLib]";
    private Registry registry;

    @Override
    public void onEnable() {
        plugin = this;
        new Config();
        this.registry = new Registry();
        new Commands();
    }

    @Override
    public void onDisable() {

    }

    public static CozyLib getInstance() {
        return plugin;
    }
    public Registry getRegistry() { return registry; }

    public static String getPluginName() { return plugin.getName(); }
    public static String getPrefix() { return prefix; }
}