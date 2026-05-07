package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.CozyLib;
import net.cozyvanilla.cozylib.common.interfaces.CraftEngineAPI;
import net.cozyvanilla.cozylib.integrations.AbstractIntegration;
import net.momirealms.craftengine.bukkit.api.event.CraftEngineReloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class CraftEngine extends AbstractIntegration implements Listener, CraftEngineAPI {

    private CraftEngineFurniture furniture;
    private CraftEngineItem item;
    private CraftEngineUtil util;

    public CraftEngine() {
        super("CraftEngine");
    }

    @EventHandler
    public void onCraftEngineReload(CraftEngineReloadEvent e) {
        if (e.isFirstReload()) ready();
    }

    @Override
    public void enable() {
        CozyLib.getInstance().getServer().getPluginManager().registerEvents(this, CozyLib.getInstance());
    }

    @Override
    protected void loadAPIs() {
        furniture = new CraftEngineFurniture();
        item = new CraftEngineItem();
        util = new CraftEngineUtil();
    }

    @Override
    public CraftEngineFurniture furniture() {
        require();
        return furniture;
    }

    @Override
    public CraftEngineItem item() {
        require();
        return item;
    }

    @Override
    public CraftEngineUtil util() {
        require();
        return util;
    }
}