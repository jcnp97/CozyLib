package net.cozyvanilla.cozylib.modules;

import dev.jorel.commandapi.CommandAPICommand;
import net.cozyvanilla.cozylib.Config;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.messages.Messages;
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

        Console.info("", "=======================================");
        Console.info("", "Modules Loaded:");

        // MESSAGES MODULE
        if (Config.isEnabled("messages")) {
            messages.enable();
            Console.info("", "- Player/Broadcast Messages");
        } else {
            Console.severe("", "- Player/Broadcast Messages");
        }

        // SEASONS MODULE
        if (Config.isEnabled("seasons")) {
            seasons.enable();
            commands.add(seasons.getCommands().get());
            Console.info("", "- CozySeasons");
        } else {
            Console.severe("", "- CozySeasons");
        }

        // SQLITE MODULE
        if (Config.isEnabled("sqlite")) {
            Console.info("", "- SQLite Storage");
        } else {
            Console.severe("", "- SQLite Storage");
        }

        // MYSQL MODULE
        if (Config.isEnabled("mysql")) {
            Console.info("", "- MySQL Database");
        } else {
            Console.severe("", "- MySQL Database");
        }

        // REDIS MODULE
        if (Config.isEnabled("redis")) {
            Console.info("", "- Redis/Cross-Server Syncing");
        } else {
            Console.severe("", "- Redis/Cross-Server Syncing");
        }

        Console.info("", "=======================================");

        // register all enabled module's commands
        registerCommands();
    }

    public void disable() {
        console.disable();
        messages.disable();
        seasons.disable();
    }

    private void registerCommands() {
        CommandAPICommand main = new CommandAPICommand(Config.getCommandPrefix());
        for (CommandAPICommand command : commands) {
            main.withSubcommand(command);
        }

        main.register();
    }

    public Seasons getSeasons() { return seasons; }
}