package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.utilities.bukkit.EntityUtils;
import net.momirealms.craftengine.bukkit.api.CraftEngineFurniture;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurniture;
import net.momirealms.craftengine.core.entity.furniture.*;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.util.Color;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CraftEngineFurnitureUtils {

    private static BukkitFurniture placeFurniture(Location location, Key key, boolean sound) {
        FurnitureDefinition furniture = CraftEngineFurniture.byId(key);
        if (furniture != null) {
            return CraftEngineFurniture.place(location, key, furniture.anyVariantName(), sound);
        }

        return null;
    }

    private static void hasFurniture(Location location) {
        Entity entity = EntityUtils.getNearest(location, null);
        if (entity != null) {
            throw new IllegalArgumentException("Failed to place a CraftEngine furniture because the location has an existing entity!");
        }
    }

    @Nullable
    private static BukkitFurniture getFurnitureFromEntity(@NotNull Entity entity) {
        return CraftEngineFurniture.getLoadedFurnitureByMetaEntity(entity);
    }

    private static boolean setColor(Entity entity, Color color) {
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
    public static Entity place(Location location, Key key, boolean sound) {
        CraftEngine.requireReady();
        hasFurniture(location);

        BukkitFurniture furniture = placeFurniture(location, key, sound);
        if (furniture != null) {
            return furniture.bukkitEntity();
        }

        return null;
    }

    public static boolean remove(Entity entity) {
        CraftEngine.requireReady();
        if (CraftEngineFurniture.isFurniture(entity)) {
            return CraftEngineFurniture.remove(entity);
        }

        return false;
    }

    public static boolean setColorFromHex(Entity entity, String hex) {
        return setColor(entity, CraftEngineColorUtils.fromHex(hex));
    }

    public static boolean setColorFromRGB(Entity entity, String rgb) {
        return setColor(entity, CraftEngineColorUtils.fromRGB(rgb));
    }
}