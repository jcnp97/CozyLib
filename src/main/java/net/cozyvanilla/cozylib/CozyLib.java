package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.modules.Modules;
import org.bukkit.plugin.java.JavaPlugin;

public final class CozyLib extends JavaPlugin {
    private static CozyLib instance;

    @Override
    public void onEnable() {
        instance = this;
        new Config(this);
        new Modules(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    public static CozyLib getInstance() { return instance; }
}