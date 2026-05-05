package net.cozyvanilla.cozylib;

import net.cozyvanilla.cozylib.integrations.Integrations;
import net.cozyvanilla.cozylib.modules.Modules;
import net.cozyvanilla.cozylib.runtime.MySQLConnection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CozyLib extends JavaPlugin {
    private static CozyLib instance;
    private static File directory;

    private Config config;
    private MySQLConnection database;
    private Logger logger;
    private Modules modules;
    private Integrations integrations;

    @Override
    public void onEnable() {
        instance = this;
        directory = getDataFolder();

        this.config = new Config(this);
        this.logger = new Logger(this);
        this.database = new MySQLConnection(this);
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