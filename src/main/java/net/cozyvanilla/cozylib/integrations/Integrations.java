package net.cozyvanilla.cozylib.integrations;

import net.cozyvanilla.cozylib.common.interfaces.CraftEngineAPI;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.common.interfaces.DiscordSRVAPI;
import net.cozyvanilla.cozylib.common.interfaces.ExcellentEconomyAPI;
import net.cozyvanilla.cozylib.common.interfaces.LuckPermsAPI;
import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngine;
import net.cozyvanilla.cozylib.integrations.discordsrv.DiscordSRV;
import net.cozyvanilla.cozylib.integrations.economy.ExcellentEconomy;
import net.cozyvanilla.cozylib.integrations.permission.LuckPerms;
import net.cozyvanilla.cozylib.modules.util.Console;
import org.bukkit.plugin.Plugin;

public class Integrations {
    private final Plugin plugin;
    private static Integrations integrations;

    private CraftEngine craftEngine;
    private DiscordSRV discordSRV;
    private ExcellentEconomy excellentEconomy;
    private LuckPerms luckPerms;

    public Integrations(Plugin plugin) {
        this.plugin = plugin;
        integrations = this;
        register();
    }

    private void register() {
        // integrations are always registered regardless of enable/disable status
        this.craftEngine = new CraftEngine();
        this.discordSRV = new DiscordSRV();
        this.excellentEconomy = new ExcellentEconomy();
        this.luckPerms = new LuckPerms();

        // register listeners
        github.scarsz.discordsrv.DiscordSRV.api.subscribe(discordSRV);

        Console.print("", MessageType.INFO);
        //Console.print("<bold>---------------------------------------", MessageType.INFO);
        Console.print("<bold>Integrations Loaded:", MessageType.INFO);

        // CRAFT ENGINE
        String craftEngineStatus = craftEngine.getInitialStatus();
        if (craftEngineStatus == null) craftEngine.enable();
        statusMessage(craftEngineStatus, craftEngine.getName());

        // DISCORD_SRV
        String discordSRVStatus = discordSRV.getInitialStatus();
        if (discordSRVStatus == null) discordSRV.enable();
        statusMessage(discordSRVStatus, discordSRV.getName());

        // EXE_ECO
        String ecoStatus = excellentEconomy.getInitialStatus();
        if (ecoStatus == null) excellentEconomy.enable();
        statusMessage(ecoStatus, excellentEconomy.getName());

        // LUCK_PERMS
        String permStatus = luckPerms.getInitialStatus();
        if (permStatus == null) luckPerms.enable();
        statusMessage(permStatus, luckPerms.getName());

        Console.print("<bold>---------------------------------------", MessageType.INFO);
    }

    public void disable() {
        craftEngine.disable();
        discordSRV.disable();
        excellentEconomy.disable();
        luckPerms.disable();
    }

    private void statusMessage(String message, String name) {
        if (message == null) {
            Console.print("[+] " + name, MessageType.INFO);
        } else  {
            Console.print("[-] " + name + " -> " + message,  MessageType.SEVERE);
        }
    }

    // ------------ public api ------------
    public static Integrations get() { return integrations; }

    // ------------ integrations ------------
    public CraftEngineAPI craftEngine() { return craftEngine; }
    public DiscordSRVAPI discordSRV() { return discordSRV; }
    public ExcellentEconomyAPI excellentEconomy() { return excellentEconomy; }
    public LuckPermsAPI luckPerms() { return luckPerms; }
}