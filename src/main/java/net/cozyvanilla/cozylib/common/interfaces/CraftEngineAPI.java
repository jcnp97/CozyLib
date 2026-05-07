package net.cozyvanilla.cozylib.common.interfaces;

import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngineFurniture;
import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngineItem;
import net.cozyvanilla.cozylib.integrations.craftengine.CraftEngineUtil;

public interface CraftEngineAPI {
    CraftEngineFurniture furniture();
    CraftEngineItem item();
    CraftEngineUtil util();
}