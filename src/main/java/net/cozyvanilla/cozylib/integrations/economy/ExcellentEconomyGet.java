package net.cozyvanilla.cozylib.integrations.economy;

import org.bukkit.entity.Player;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;
import su.nightexpress.excellenteconomy.api.currency.ExcellentCurrency;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ExcellentEconomyGet {
    private final ExcellentEconomyAPI api;

    public ExcellentEconomyGet(ExcellentEconomyAPI api) { this.api = api; }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    @Nullable
    public CompletableFuture<Double> getBalanceAsync(UUID uuid, String currency) {
        if (api.canPerformOperations()) {
            return api.getBalanceAsync(uuid, currency);
        }

        return null;
    }

    public CompletableFuture<Double> getBalanceAsync(UUID uuid) {
        return getBalanceAsync(uuid, "money");
    }

    public double getBalance(Player player, String currency) {
        return api.getBalance(player, currency);
    }

    public double getBalance(Player player) {
        return getBalance(player, "money");
    }

    public List<String> getCurrencies() {
        List<String> list = new ArrayList<>();
        for (ExcellentCurrency currency : api.getCurrencies()) {
            list.add(currency.getId());
        }

        return list;
    }
}