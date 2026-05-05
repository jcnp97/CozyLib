package net.cozyvanilla.cozylib.integrations;

import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngine;
import net.cozyvanilla.cozylib.integrations.discordsrv.DiscordSRV;
import net.cozyvanilla.cozylib.integrations.economy.ExcellentEconomy;
import net.cozyvanilla.cozylib.integrations.permission.LuckPerms;
import net.cozyvanilla.cozylib.modules.util.Console;
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

        Console.print("", MessageType.INFO);
        Console.print("<bold>Integrations Loaded:", MessageType.INFO);
        Console.print("<bold>---------------------------------------", MessageType.INFO);

        // CRAFT ENGINE
        if (Config.hasIntegration("craft_engine")) {
            craftEngine.enable();
            Console.print("[+] " + craftEngine.getName(), MessageType.INFO);
        } else {
            Console.print("[-] " + craftEngine.getName(),  MessageType.SEVERE);
        }

        // DISCORD_SRV
        if (Config.hasIntegration("discord_srv")) {
            discordSRV.enable();
            Console.print("[+] " + discordSRV.getName(), MessageType.INFO);
        } else {
            Console.print("[-] " + discordSRV.getName(), MessageType.SEVERE);
        }

        // EXE_ECO
        if (Config.hasIntegration("excellent_economy")) {
            excellentEconomy.enable();
            Console.print("[+] " + excellentEconomy.getName(), MessageType.INFO);
        } else {
            Console.print("[-] " + excellentEconomy.getName(), MessageType.SEVERE);
        }

        // LUCK_PERMS
        if (Config.hasIntegration("luck_perms")) {
            luckPerms.enable();
            Console.print("[+] " + luckPerms.getName(), MessageType.INFO);
        } else {
            Console.print("[-] " + luckPerms.getName(), MessageType.SEVERE);
        }

        Console.print("<bold>---------------------------------------", MessageType.INFO);
    }

    public void disable() {
        craftEngine.disable();
        discordSRV.disable();
        excellentEconomy.disable();
        luckPerms.disable();
    }
}