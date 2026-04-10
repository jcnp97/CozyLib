package net.cozyvanilla.cozylib;

import org.bukkit.plugin.java.JavaPlugin;

public final class CozyLib extends JavaPlugin {

    @Override
    public void onEnable() {
        new Config(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}