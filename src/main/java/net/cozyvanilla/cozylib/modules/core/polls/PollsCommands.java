package net.cozyvanilla.cozylib.modules.core.polls;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.cozyvanilla.cozylib.common.enums.MessageType;
import net.cozyvanilla.cozylib.modules.util.Console;
import net.cozyvanilla.cozylib.modules.util.Messages;
import org.bukkit.entity.Player;

public class PollsCommands {
    private final Polls polls;
    private final String permission = "cozylib.admin";

    public PollsCommands(Polls polls) {
        this.polls = polls;
    }

    public CommandAPICommand get() {
        return new CommandAPICommand("polls")
                .withSubcommand(start())
                .withSubcommand(addValue())
                .withSubcommand(addPoll());
    }

    private CommandAPICommand start() {
        return new CommandAPICommand("start")
                .withArguments(new IntegerArgument("expires_after_days"))
                .withPermission(permission)
                .executes((sender, args) -> {

                    if (polls.hasStarted()) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "You cannot start a new poll because it has already started!", MessageType.SEVERE);
                        } else {
                            Console.print(polls.getPrefix(), "You cannot start a new poll because it has already started!", MessageType.SEVERE);
                        }

                        return;
                    }

                    if (polls.isEmpty()) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "You cannot start a poll because it is empty!", MessageType.SEVERE);
                        } else {
                            Console.print(polls.getPrefix(), "You cannot start a poll because it is empty!",  MessageType.SEVERE);
                        }

                        return;
                    }

                    int days = (int) args.get("expires_after_days");
                    if (days > 0) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "The poll has been started and expires in " + days + " days!", MessageType.INFO);
                        } else {
                            Console.print(polls.getPrefix(), "The poll has been started and expires in " + days + " days!", MessageType.INFO);
                        }

                        polls.startPoll(days);
                        return;
                    }

                    if (sender instanceof Player player) {
                        Messages.message(player, "Expiration days must be positive!", MessageType.SEVERE);
                    } else {
                        Console.print(polls.getPrefix(), "Expiration days must be positive!",  MessageType.SEVERE);
                    }
                });
    }

    private CommandAPICommand addValue() {
        return new CommandAPICommand("donate")
                .withArguments(new StringArgument("poll_name"), new DoubleArgument("amount"))
                .withPermission(permission)
                .executes((sender, args) -> {

                    String pollName = (String) args.get("poll_name");
                    if (!polls.exists(pollName)) {
                        if (sender instanceof Player player) {
                            Messages.message(player,  pollName + " does not exist from the current polls!", MessageType.SEVERE);
                        } else {
                            Console.print(polls.getPrefix(), pollName + " does not exist from the current polls!",  MessageType.SEVERE);
                        }

                        return;
                    }

                    double amount = (Double) args.get("amount");
                    if (amount <= 0) {
                        if (sender instanceof Player player) {
                            Messages.message(player,  "Amount must be greater than 0!", MessageType.SEVERE);
                        } else {
                            Console.print(polls.getPrefix(), "Amount must be greater than 0!",  MessageType.SEVERE);
                        }

                        return;
                    }

                    if (polls.addValue(pollName, amount)) {
                        // todo: get economy
                    }
                });
    }

    private CommandAPICommand addPoll() {
        return new CommandAPICommand("add")
                .withArguments(new StringArgument("poll_name"))
                .withPermission(permission)
                .executes((sender, args) -> {

                    String pollName = (String) args.get("poll_name");
                    if (polls.hasStarted()) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "You cannot add a new poll because it has already started!", MessageType.SEVERE);
                        } else {
                            Console.print(polls.getPrefix(), "You cannot add a new poll because it has already started!",  MessageType.SEVERE);
                        }

                        return;
                    }

                    if (polls.exists(pollName)) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "Poll with name " + pollName + " already exists!", MessageType.SEVERE);
                        } else {
                            Console.print(polls.getPrefix(), "Poll with name " + pollName + " already exists!", MessageType.SEVERE);
                        }

                        return;
                    }

                    if (pollName != null && !pollName.isEmpty()) {
                        if (sender instanceof Player player) {
                            Messages.message(player, pollName + " has been added.", MessageType.INFO);
                        } else {
                            Console.print(polls.getPrefix(), pollName + " has been added.", MessageType.INFO);
                        }

                        polls.addPoll(pollName);
                        return;
                    }

                    if (sender instanceof Player player) {
                        Messages.message(player, "Poll name cannot be empty!", MessageType.SEVERE);
                    } else {
                        Console.print(polls.getPrefix(), "Poll name cannot be empty!",  MessageType.SEVERE);
                    }
                });
    }
}