package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.integrations.Integrations;
import net.cozyvanilla.cozylib.runtime.Logger;
import net.cozyvanilla.cozylib.util.bukkit.EntityUtils;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurniture;
import net.momirealms.craftengine.core.entity.furniture.*;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.util.Color;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CraftEngineFurniture {

    public CraftEngineFurniture() {}

    private BukkitFurniture placeFurniture(Location location, Key key, boolean sound) {
        FurnitureDefinition furniture = net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.byId(key);
        if (furniture != null) {
            return net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.place(location, key, furniture.anyVariantName(), sound);
        }

        return null;
    }

    private void hasFurniture(Location location) {
        org.bukkit.entity.Entity entity = EntityUtils.getNearest(location, null);
        if (entity != null) {
            Logger.warning("Failed to place a furniture because the location has an existing entity");
        }
    }

    @Nullable
    private BukkitFurniture getFurnitureFromEntity(@NotNull org.bukkit.entity.Entity entity) {
        return net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.getLoadedFurnitureByMetaEntity(entity);
    }

    private boolean setColor(org.bukkit.entity.Entity entity, Color color) {
        BukkitFurniture furniture = getFurnitureFromEntity(entity);
        if (furniture == null) { return false; }

        // apply color to furniture item
        Item furnitureItem = furniture.sourceItem();
        if (furnitureItem == null) { return false; }
        furnitureItem.dyedColor(color);

        // apply tint to furniture entity
        furniture.refreshElements();

        return true;
    }

    // public API
    @Nullable
    public org.bukkit.entity.Entity place(Location location, Key key, boolean sound) {
        hasFurniture(location);

        BukkitFurniture furniture = placeFurniture(location, key, sound);
        if (furniture != null) {
            return furniture.bukkitEntity();
        }

        return null;
    }

    public boolean remove(org.bukkit.entity.Entity entity) {
        if (net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.isFurniture(entity)) {
            return net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.remove(entity);
        }

        return false;
    }

    public boolean setColorFromHex(org.bukkit.entity.Entity entity, String hex) {
        return setColor(entity, Integrations.get().craftEngine().util().fromHex(hex));
    }

    public boolean setColorFromRGB(org.bukkit.entity.Entity entity, String rgb) {
        return setColor(entity, Integrations.get().craftEngine().util().fromRGB(rgb));
    }
}