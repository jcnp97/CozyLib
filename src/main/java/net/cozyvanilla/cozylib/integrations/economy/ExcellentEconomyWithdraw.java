package net.cozyvanilla.cozylib.integrations.economy;

import org.bukkit.entity.Player;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

public class ExcellentEconomyWithdraw {
    private final ExcellentEconomyAPI api;

    public ExcellentEconomyWithdraw(ExcellentEconomyAPI api) {
        this.api = api;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public boolean withdraw(Player player, String currency, double amount) {
        return api.withdraw(player, currency, amount);
    }

    public boolean withdraw(Player player, double amount) {
        return withdraw(player, "money", amount);
    }
}