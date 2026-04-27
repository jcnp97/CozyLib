package net.cozyvanilla.cozylib.integrations;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngine;
import net.cozyvanilla.cozylib.integrations.discordsrv.DiscordSRV;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.plugin.Plugin;

public class Integrations {
    private final Plugin plugin;

    private CraftEngine craftEngine;
    private DiscordSRV discordSRV;

    public Integrations(Plugin plugin) {
        this.plugin = plugin;
        register();
    }

    private void register() {
        // integrations are always registered regardless of enable/disable status
        this.craftEngine = new CraftEngine(plugin);
        this.discordSRV = new DiscordSRV();

        Console.info("");
        Console.info("<bold>Integrations Loaded:");
        Console.info("<bold>---------------------------------------");

        // CRAFT ENGINE
        if (Config.hasIntegration("craft_engine")) {
            craftEngine.enable();
            Console.info("[+] " + craftEngine.getName());
        } else {
            Console.severe("[-] " + craftEngine.getName());
        }

        // DISCORD_SRV
        if (Config.hasIntegration("discord_srv")) {
            discordSRV.enable();
            Console.info("[+] " + discordSRV.getName());
        } else {
            Console.severe("[-] " + discordSRV.getName());
        }

        Console.info("<bold>---------------------------------------");
    }

    public void disable() {
        craftEngine.disable();
        discordSRV.disable();
    }
}