package net.cozyvanilla.cozylib.integrations.discordsrv;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import net.cozyvanilla.cozylib.common.interfaces.DiscordSRVAPI;
import net.cozyvanilla.cozylib.integrations.AbstractIntegration;

public final class DiscordSRV extends AbstractIntegration implements DiscordSRVAPI {

    private DiscordSRVUtil util;

    public DiscordSRV() {
        super("DiscordSRV");
    }

    @Subscribe
    public void onDiscordReady(DiscordReadyEvent event) {
        require();
        ready();
    }

    @Override
    public void enable() {}

    @Override
    protected void loadAPIs() {
        util = new DiscordSRVUtil();
    }

    @Override
    public DiscordSRVUtil util() {
        require();
        return util;
    }
}