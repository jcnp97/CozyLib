package asia.virtualmc.cozylib.commands;

import asia.virtualmc.cozylib.Enums;
import asia.virtualmc.cozylib.integrations.IFUtils;
import asia.virtualmc.cozylib.utilities.bukkit.messages.MessageUtils;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

public class GUICommands {

    public static CommandAPICommand sendConfirmGUI() {
        return new CommandAPICommand("confirm_gui")
                .withPermission("cozylib.admin")
                .executes((sender, args) -> {
                    if (sender instanceof Player player) {
                        IFUtils.confirmGui(player, result -> {
                            if (result) {
                                MessageUtils.sendMessage(player, "You have confirmed the action.", Enums.MessageType.INFO);
                            } else {
                                MessageUtils.sendMessage(player, "You have cancelled the action.", Enums.MessageType.SEVERE);
                            }
                        });
                    } else {
                        sender.sendMessage("This command can only be used by players.");
                    }
                });
    }
}
