package asia.virtualmc.cozylib;

import asia.virtualmc.vLibCore.libs.commandapi.CommandAPI;
import asia.virtualmc.vLibCore.libs.commandapi.CommandAPIPaperConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class CozyLib extends JavaPlugin {
    private static CozyLib plugin;
    private static final String prefix = "[CozyLib]";
    private Registry registry;

    @Override
    public void onEnable() {
        plugin = this;
        CommandAPI.onEnable();
        new Config();
        //this.registry = new Registry(this);
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIPaperConfig(this)
                .verboseOutput(false)
                .silentLogs(true)
        );
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