package net.cozyvanilla.cozylib.integrations.economy;

import net.cozyvanilla.cozylib.common.interfaces.ExcellentEconomyAPI;
import net.cozyvanilla.cozylib.integrations.AbstractIntegration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class ExcellentEconomy extends AbstractIntegration implements ExcellentEconomyAPI {

    private su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI api;
    private ExcellentEconomyGet get;
    private ExcellentEconomyDeposit deposit;
    private ExcellentEconomyWithdraw withdraw;

    public ExcellentEconomy() {
        super("ExcellentEconomy");
    }

    @Override
    public void enable() {
        RegisteredServiceProvider<su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI> provider = Bukkit.getServer().getServicesManager().getRegistration(su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI.class);
        if (provider != null) {
            api = provider.getProvider();
            ready();
        }
    }

    @Override
    protected void loadAPIs() {
        get = new ExcellentEconomyGet(api);
        deposit = new ExcellentEconomyDeposit(api);
        withdraw = new ExcellentEconomyWithdraw(api);
    }

    @Override
    public ExcellentEconomyGet get() {
        require();
        return get;
    }

    @Override
    public ExcellentEconomyWithdraw withdraw() {
        require();
        return withdraw;
    }

    @Override
    public ExcellentEconomyDeposit deposit() {
        require();
        return deposit;
    }
}
