package net.cozyvanilla.cozylib.modules;

import dev.jorel.commandapi.CommandAPICommand;
import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.modules.util.Console;
import net.cozyvanilla.cozylib.runtime.MySQLConnection;
import net.cozyvanilla.cozylib.modules.core.polls.Polls;
import net.cozyvanilla.cozylib.modules.core.seasons.Seasons;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class Modules {
    private final Plugin plugin;
    private final Set<CommandAPICommand> commands = new HashSet<>();

    // ------------ mandatory ------------
    private MySQLConnection mysqlDatabase;

    // ------------ optional ------------
    private Seasons seasons;
    private Polls polls;

    public Modules(Plugin plugin) {
        this.plugin = plugin;
        register();
    }

    private void register() {
        // mandatory
        this.mysqlDatabase = new MySQLConnection(plugin);
        mysqlDatabase.enable();


        this.seasons = new Seasons(plugin);
        this.polls = new Polls(plugin);

        Console.print("<bold>Modules Loaded:", MessageType.INFO);
        Console.print("<bold>---------------------------------------", MessageType.INFO);

        // SEASONS MODULE
        if (Config.hasModule("seasons")) {
            seasons.enable();
            commands.add(seasons.getCommands().get());
            Console.print("[+] " + seasons.getName(), MessageType.INFO);
        } else {
            Console.print("[-] " + seasons.getName(), MessageType.SEVERE);
        }

        // POLLS MODULE
        if (Config.hasModule("polls")) {
            polls.enable();
            commands.add(polls.getCommands().get());
            Console.print("[+] " + polls.getName(), MessageType.INFO);
        } else {
            Console.print("[-] " + polls.getName(), MessageType.SEVERE);
        }

        Console.print("<bold>---------------------------------------", MessageType.INFO);

        // register all enabled module's commands
        registerCommands();
    }

    public void disable() {
        seasons.disable();
        polls.disable();
    }

    private void registerCommands() {
        if (commands.isEmpty()) {
            Console.print("", "No commands to register for /" + Config.getCommandPrefix(), MessageType.SEVERE);
            return;
        }

        CommandAPICommand main = new CommandAPICommand(Config.getCommandPrefix());
        for (CommandAPICommand command : commands) {
            main.withSubcommand(command);
        }

        main.register();
    }

    public Seasons getSeasons() { return seasons; }
}