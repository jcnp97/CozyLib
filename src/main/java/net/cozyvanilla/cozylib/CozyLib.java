package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.integrations.Integrations;
import net.cozyvanilla.cozylib.modules.Modules;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CozyLib extends JavaPlugin {
    private static CozyLib instance;
    private static File directory;

    private Logger logger;
    private Modules modules;
    private Integrations integrations;

    @Override
    public void onEnable() {
        instance = this;
        directory = getDataFolder();

        new Config(this);
        this.logger = new Logger(this);
        this.modules = new Modules(this);
        this.integrations = new Integrations(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        modules.disable();
        integrations.disable();
        logger.disable();
    }

    public static CozyLib getInstance() { return instance; }
    public static File getDirectory() { return directory; }
}