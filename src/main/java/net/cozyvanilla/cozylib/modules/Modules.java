package net.cozyvanilla.cozylib.modules;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.messages.Messages;
import org.bukkit.plugin.Plugin;

public class Modules {
    private final Plugin plugin;

    public Modules(Plugin plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        new Console(plugin);

        Console.info("", "=======================================");

        Console.info("", "Modules Loaded:");
        if (Config.isEnabled("messages")) {
            new Messages(plugin);
            Console.info("", "- Player/Broadcast Messages");
        } else {
            Console.severe("", "- Player/Broadcast Messages");
        }

        if (Config.isEnabled("sqlite")) {
            Console.info("", "- SQLite Storage");
        } else {
            Console.severe("", "- SQLite Storage");
        }

        if (Config.isEnabled("mysql")) {
            Console.info("", "- MySQL Database");
        } else {
            Console.severe("", "- MySQL Database");
        }

        if (Config.isEnabled("redis")) {
            Console.info("", "- Redis/Cross-Server Syncing");
        } else {
            Console.severe("", "- Redis/Cross-Server Syncing");
        }

        Console.info("", "=======================================");
    }
}
