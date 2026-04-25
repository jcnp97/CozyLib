package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.integrations.Integrations;
import net.cozyvanilla.cozylib.modules.Modules;
import org.bukkit.plugin.java.JavaPlugin;

public final class CozyLib extends JavaPlugin {
    private static CozyLib instance;

    private Modules modules;
    private Integrations integrations;

    @Override
    public void onEnable() {
        instance = this;

        new Config(this);
        this.modules = new Modules(this);
        this.integrations = new Integrations(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        modules.disable();
        integrations.disable();
    }

    public static CozyLib getInstance() { return instance; }
}