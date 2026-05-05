//package net.cozyvanilla.cozylib.modules.player_data;
//
//import dev.jorel.commandapi.CommandAPICommand;
//import dev.jorel.commandapi.arguments.ArgumentSuggestions;
//import dev.jorel.commandapi.arguments.StringArgument;
//import net.cozyvanilla.cozylib.Enums;
//import net.cozyvanilla.cozylib.modules.util.Console;
//import net.cozyvanilla.cozylib.modules.util.Messages;
//import net.cozyvanilla.cozylib.modules.core.seasons.SeasonsAPI;
//import org.bukkit.entity.Player;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PlayerDataCommands {
//    private final PlayerData seasons;
//    private final String permission = "cozylib.admin";
//
//    public PlayerDataCommands(PlayerData seasons) {
//        this.seasons = seasons;
//    }
//
//    public CommandAPICommand get() {
//        return new CommandAPICommand("seasons")
//                .withSubcommand(set())
//                .withSubcommand(reset());
//    }
//
//    private CommandAPICommand set() {
//        List<String> seasonsList = new ArrayList<>();
//        seasonsList.add("spring");
//        seasonsList.add("summer");
//        seasonsList.add("fall");
//        seasonsList.add("winter");
//
//        return new CommandAPICommand("set")
//                .withArguments(new StringArgument("season_name").replaceSuggestions(
//                        ArgumentSuggestions.strings(seasonsList)))
//                .withPermission(permission)
//                .executes((sender, args) -> {
//
//                    String seasonName = (String) args.get("season_name");
//                    if (seasonName == null) {
//                        if (sender instanceof Player player) {
//                            Messages.message(player, "Season name cannot be empty!", Enums.MessageType.SEVERE);
//                        } else {
//                            Console.severe(seasons.getPrefix(), "Season name cannot be empty!");
//                        }
//
//                        return;
//                    }
//
//                    seasons.set(SeasonsAPI.toEnum(seasonName));
//                });
//    }
//
//    private CommandAPICommand reset() {
//        return new CommandAPICommand("reset")
//                .withPermission(permission)
//                .executes((sender, args) -> {
//
//                    if (sender instanceof Player player) {
//                        Messages.message(player, "CozySeasons schedule rotation has been reset!", Enums.MessageType.INFO);
//                    } else {
//                        Console.info(seasons.getPrefix(), "CozySeasons schedule rotation has been reset!");
//                    }
//
//                    seasons.reset();
//                });
//    }
//}
