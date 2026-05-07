package net.cozyvanilla.cozylib.integrations.permission;

import net.cozyvanilla.cozylib.common.interfaces.LuckPermsAPI;
import net.cozyvanilla.cozylib.integrations.AbstractIntegration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class LuckPerms extends AbstractIntegration implements LuckPermsAPI {

    private net.luckperms.api.LuckPerms api;
    private LuckPermsGet get;

    public LuckPerms() {
        super("LuckPerms");
    }

    @Override
    public void enable() {
        RegisteredServiceProvider<net.luckperms.api.LuckPerms> provider = Bukkit.getServicesManager().getRegistration(net.luckperms.api.LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
            ready();
        }
    }

    @Override
    protected void loadAPIs() {
        get = new LuckPermsGet(api);
    }

    @Override
    public LuckPermsGet get() {
        require();
        return get;
    }
}
