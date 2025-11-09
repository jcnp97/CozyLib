package asia.virtualmc.cozylib;

import asia.virtualmc.cozylib.commands.GUICommands;
import dev.jorel.commandapi.CommandAPICommand;

public class Commands {

    public Commands() {
        CommandAPICommand command = new CommandAPICommand("cozylib");


        command.withSubcommand(GUICommands.sendConfirmGUI());
        command.register();
    }
 }
