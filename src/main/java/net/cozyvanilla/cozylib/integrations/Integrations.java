package net.cozyvanilla.cozylib.integrations;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngine;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.plugin.Plugin;

public class Integrations {
    private final Plugin plugin;

    private CraftEngine craftEngine;

    public Integrations(Plugin plugin) {
        this.plugin = plugin;
        register();
    }

    private void register() {
        // integrations are always registered regardless of enable/disable status
        this.craftEngine = new CraftEngine(plugin);

        Console.info("", "=======================================");
        Console.info("", "Integrations Loaded:");

        // CRAFT ENGINE
        if (Config.isEnabled("craft_engine")) {
            craftEngine.enable();
            Console.info("", "- " + craftEngine.getName());
        } else {
            Console.severe("", "- " + craftEngine.getName());
        }

        Console.info("", "=======================================");
    }

    public void disable() {
        craftEngine.disable();
    }
}