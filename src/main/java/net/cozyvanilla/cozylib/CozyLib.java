package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.modules.Modules;
import org.bukkit.plugin.java.JavaPlugin;

public final class CozyLib extends JavaPlugin {

    @Override
    public void onEnable() {
        new Config(this);
        new Modules(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}