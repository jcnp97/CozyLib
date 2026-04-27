package net.cozyvanilla.cozylib.modules.polls;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.messages.Messages;
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
                            Messages.message(player, "You cannot start a new poll because it has already started!", Enums.MessageType.SEVERE);
                        } else {
                            Console.severe(polls.getPrefix(), "You cannot start a new poll because it has already started!");
                        }

                        return;
                    }

                    if (polls.isEmpty()) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "You cannot start a poll because it is empty!", Enums.MessageType.SEVERE);
                        } else {
                            Console.severe(polls.getPrefix(), "You cannot start a poll because it is empty!");
                        }

                        return;
                    }

                    int days = (int) args.get("expires_after_days");
                    if (days > 0) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "The poll has been started and expires in " + days + " days!", Enums.MessageType.INFO);
                        } else {
                            Console.info(polls.getPrefix(), "The poll has been started and expires in " + days + " days!");
                        }

                        polls.startPoll(days);
                        return;
                    }

                    if (sender instanceof Player player) {
                        Messages.message(player, "Expiration days must be positive!", Enums.MessageType.SEVERE);
                    } else {
                        Console.severe(polls.getPrefix(), "Expiration days must be positive!");
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
                            Messages.message(player,  pollName + " does not exist from the current polls!", Enums.MessageType.SEVERE);
                        } else {
                            Console.severe(polls.getPrefix(), pollName + " does not exist from the current polls!");
                        }

                        return;
                    }

                    double amount = (Double) args.get("amount");
                    if (amount <= 0) {
                        if (sender instanceof Player player) {
                            Messages.message(player,  "Amount must be greater than 0!", Enums.MessageType.SEVERE);
                        } else {
                            Console.severe(polls.getPrefix(), "Amount must be greater than 0!");
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
                            Messages.message(player, "You cannot add a new poll because it has already started!", Enums.MessageType.SEVERE);
                        } else {
                            Console.severe(polls.getPrefix(), "You cannot add a new poll because it has already started!");
                        }

                        return;
                    }

                    if (polls.exists(pollName)) {
                        if (sender instanceof Player player) {
                            Messages.message(player, "Poll with name " + pollName + " already exists!", Enums.MessageType.SEVERE);
                        } else {
                            Console.severe(polls.getPrefix(), "Poll with name " + pollName + " already exists!");
                        }

                        return;
                    }

                    if (pollName != null && !pollName.isEmpty()) {
                        if (sender instanceof Player player) {
                            Messages.message(player, pollName + " has been added.", Enums.MessageType.INFO);
                        } else {
                            Console.info(polls.getPrefix(), pollName + " has been added.");
                        }

                        polls.addPoll(pollName);
                        return;
                    }

                    if (sender instanceof Player player) {
                        Messages.message(player, "Poll name cannot be empty!", Enums.MessageType.SEVERE);
                    } else {
                        Console.severe(polls.getPrefix(), "Poll name cannot be empty!");
                    }
                });
    }
}