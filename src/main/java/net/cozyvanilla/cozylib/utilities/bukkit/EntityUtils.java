package net.cozyvanilla.cozylib.utilities.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityUtils {

    /**
     * Converts an entity to its UUID string.
     *
     * @param entity the entity to convert
     * @return the entity UUID as a string, or null if entity is null
     */
    @Nullable
    public static String toString(Entity entity) {
        if (entity == null) return null;
        return entity.getUniqueId().toString();
    }

    /**
     * Gets an entity from a UUID string.
     *
     * @param entityId the UUID string of the entity
     * @return the entity, or null if not found
     */
    @Nullable
    public static Entity toEntity(String entityId) {
        if (entityId == null) return null;
        return getEntity(UUID.fromString(entityId));
    }

    /**
     * Gets an entity by its UUID.
     *
     * @param uuid the entity UUID
     * @return the entity, or null if not found
     */
    @Nullable
    public static Entity getEntity(UUID uuid) {
        if (uuid == null) return null;
        return Bukkit.getEntity(uuid);
    }

    /**
     * Gets the UUID of an entity.
     *
     * @param entity the entity
     * @return the entity UUID
     */
    public static UUID getUUID(@NotNull Entity entity) {
        return entity.getUniqueId();
    }

    /**
     * Gets the nearest entity to a location within a maximum distance.
     *
     * @param location the location to search from
     * @param maxDistance the maximum search distance in blocks
     * @param type the entity type to match, or null for any type
     * @return the nearest matching entity, or null if none is found
     */
    @Nullable
    public static Entity getNearest(@NotNull Location location, double maxDistance, @Nullable EntityType type) {
        if (location.getWorld() == null) return null;

        double maxDistanceSquared = maxDistance * maxDistance;
        Entity nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Entity entity : location.getWorld().getNearbyEntities(location, maxDistance, maxDistance, maxDistance)) {
            if (type != null && entity.getType() != type) continue;

            double distanceSquared = entity.getLocation().distanceSquared(location);
            if (distanceSquared <= maxDistanceSquared && distanceSquared < nearestDistance) {
                nearestDistance = distanceSquared;
                nearest = entity;
            }
        }

        return nearest;
    }

    /**
     * Gets the nearest entity to a player within a maximum distance.
     *
     * @param player the player to search from
     * @param maxDistance the maximum search distance in blocks
     * @param type the entity type to match, or null for any type
     * @return the nearest matching entity, or null if none is found
     */
    public static Entity getNearest(@NotNull Player player, double maxDistance, @Nullable EntityType type) {
        return getNearest(player.getLocation(), maxDistance, type);
    }

    /**
     * Gets the UUID of a fishhook.
     *
     * @param fishHook the fishhook entity
     * @return the fishhook UUID
     */
    public static UUID getFishHookId(FishHook fishHook) {
        return fishHook.getUniqueId();
    }

    /**
     * Gets a fishhook by its UUID.
     *
     * @param uuid the fishhook UUID
     * @return the fishhook, or null if not found or not a fishhook
     */
    public static FishHook getFishHook(UUID uuid) {
        Entity entity = Bukkit.getEntity(uuid);
        if (entity instanceof FishHook fishHook) {
            return fishHook;
        }

        return null;
    }
}
