package net.cozyvanilla.cozylib.integrations.economy;

import org.bukkit.entity.Player;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;

public class ExcellentEconomyDeposit {
    private final ExcellentEconomyAPI api;

    public ExcellentEconomyDeposit(ExcellentEconomyAPI api) {
        this.api = api;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public boolean deposit(Player player, String currency, double amount) {
        return api.deposit(player, currency, amount);
    }

    public boolean deposit(Player player, double amount) {
        return deposit(player, "money", amount);
    }
}