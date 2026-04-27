package net.cozyvanilla.cozylib.modules;

import dev.jorel.commandapi.CommandAPICommand;
import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.messages.Messages;
import net.cozyvanilla.cozylib.modules.mysql.MySQLDatabase;
import net.cozyvanilla.cozylib.modules.polls.Polls;
import net.cozyvanilla.cozylib.modules.seasons.Seasons;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class Modules {
    private final Plugin plugin;
    private final Set<CommandAPICommand> commands = new HashSet<>();

    private Console console;
    private Messages messages;
    private Seasons seasons;
    private MySQLDatabase mysql;
    private Polls polls;

    public Modules(Plugin plugin) {
        this.plugin = plugin;
        register();
    }

    private void register() {
        // always enabled
        this.console = new Console(plugin);
        console.enable();

        // modules are always registered
        this.messages = new Messages(plugin);
        this.seasons = new Seasons(plugin);
        this.mysql = new MySQLDatabase(plugin);
        this.polls = new Polls(plugin);

        Console.info("<bold>Modules Loaded:");
        Console.info("<bold>---------------------------------------");

        // MESSAGES MODULE
        if (Config.isEnabled("messages")) {
            messages.enable();
            Console.info("[+] " + messages.getName());
        } else {
            Console.severe("[-] " + messages.getName());
        }

        // SEASONS MODULE
        if (Config.isEnabled("seasons")) {
            seasons.enable();
            commands.add(seasons.getCommands().get());
            Console.info("[+] " + seasons.getName());
        } else {
            Console.severe("[-] " + seasons.getName());
        }

        // MYSQL MODULE
        if (Config.isEnabled("mysql")) {
            mysql.enable();
            Console.info("[+] " + mysql.getName());
        } else {
            Console.severe("[-] " + mysql.getName());
        }

        // POLLS MODULE
        if (Config.isEnabled("polls")) {
            polls.enable();
            commands.add(polls.getCommands().get());
            Console.info("[+] " + polls.getName());
        } else {
            Console.severe("[-] " + polls.getName());
        }

        Console.info("<bold>---------------------------------------");

        // register all enabled module's commands
        registerCommands();
    }

    public void disable() {
        console.disable();
        messages.disable();
        seasons.disable();
        mysql.disable();
        polls.disable();
    }

    private void registerCommands() {
        if (commands.isEmpty()) {
            Console.severe("", "No commands to register for /" + Config.getCommandPrefix());
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