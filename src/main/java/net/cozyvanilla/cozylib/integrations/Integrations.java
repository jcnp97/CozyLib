package net.cozyvanilla.cozylib.integrations;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.builders.MiniMessageBuilder;
import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngine;
import net.cozyvanilla.cozylib.integrations.discordsrv.DiscordSRV;
import net.cozyvanilla.cozylib.integrations.economy.ExcellentEconomy;
import net.cozyvanilla.cozylib.integrations.permission.LuckPerms;
import net.cozyvanilla.cozylib.modules.messages.Console;
import org.bukkit.plugin.Plugin;

public class Integrations {
    private final Plugin plugin;

    private CraftEngine craftEngine;
    private DiscordSRV discordSRV;
    private ExcellentEconomy excellentEconomy;
    private LuckPerms luckPerms;

    public Integrations(Plugin plugin) {
        this.plugin = plugin;
        register();
    }

    private void register() {
        // integrations are always registered regardless of enable/disable status
        this.craftEngine = new CraftEngine();
        this.discordSRV = new DiscordSRV();
        this.excellentEconomy = new ExcellentEconomy();
        this.luckPerms = new LuckPerms();

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

        // EXE_ECO
        if (Config.hasIntegration("excellent_economy")) {
            excellentEconomy.enable();
            Console.info("[+] " + excellentEconomy.getName());
        } else {
            Console.severe("[-] " + excellentEconomy.getName());
        }

        // LUCK_PERMS
        if (Config.hasIntegration("luck_perms")) {
            luckPerms.enable();
            Console.info("[+] " + luckPerms.getName());
        } else {
            Console.severe("[-] " + luckPerms.getName());
        }

        Console.info("<bold>---------------------------------------");
    }

    public void disable() {
        craftEngine.disable();
        discordSRV.disable();
        excellentEconomy.disable();
        luckPerms.disable();
    }
}