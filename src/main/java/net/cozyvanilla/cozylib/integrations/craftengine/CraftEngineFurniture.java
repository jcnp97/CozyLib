package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.modules.seasons.Seasons;
import net.cozyvanilla.cozylib.utilities.bukkit.EntityUtils;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurniture;
import net.momirealms.craftengine.core.entity.furniture.*;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.util.Color;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
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
        Entity entity = EntityUtils.getNearest(location, null);
        if (entity != null) {
            throw new IllegalArgumentException("Failed to place a CraftEngine furniture because the location has an existing entity!");
        }
    }

    @Nullable
    private BukkitFurniture getFurnitureFromEntity(@NotNull Entity entity) {
        return net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.getLoadedFurnitureByMetaEntity(entity);
    }

    private boolean setColor(Entity entity, Color color) {
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
    public Entity place(Location location, Key key, boolean sound) {
        hasFurniture(location);

        BukkitFurniture furniture = placeFurniture(location, key, sound);
        if (furniture != null) {
            return furniture.bukkitEntity();
        }

        return null;
    }

    public boolean remove(Entity entity) {
        if (net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.isFurniture(entity)) {
            return net.momirealms.craftengine.bukkit.api.CraftEngineFurniture.remove(entity);
        }

        return false;
    }

    public boolean setColorFromHex(Entity entity, String hex) {
        return setColor(entity, CraftEngine.util().fromHex(hex));
    }

    public boolean setColorFromRGB(Entity entity, String rgb) {
        return setColor(entity, CraftEngine.util().fromRGB(rgb));
    }
}