package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.modules.messages.Console;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.item.BukkitItemDefinition;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.util.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.momirealms.craftengine.core.util.Key;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CraftEngineItemUtils {

    private static ItemStack getItemStack(Key key) {
        BukkitItemDefinition item = CraftEngineItems.byId(key);
        if (item == null) {
            Console.severe("Invalid CraftEngineItem id '" + key + "'");
            return null;
        }

        return item.buildBukkitItem();
    }

    private static boolean isCraftEngineItem(ItemStack itemStack) {
        return CraftEngineItems.getCustomItemId(itemStack) != null;
    }

    // public API
    @Nullable
    public static ItemStack getItemStack(String namespace, String itemName) {
        CraftEngine.requireReady();
        return getItemStack(CraftEngine.toKey(namespace, itemName));
    }

    @Nullable
    public static ItemStack getItemStack(String id) {
        CraftEngine.requireReady();
        return getItemStack(CraftEngine.toKey(id));
    }

    @Nullable
    public static Key getKey(ItemStack itemStack) {
        CraftEngine.requireReady();
        return CraftEngineItems.getCustomItemId(itemStack);
    }

    @Nullable
    public static String getItemId(@NotNull ItemStack itemStack) {
        CraftEngine.requireReady();
        Key key = getKey(itemStack);
        return key != null ? key.toString() : null;
    }

    public static Item setColor(Item item, Color color) {
        return item.dyedColor(color);
    }

    public static Item setColorFromHex(Item item, String hex) {
        return item.dyedColor(CraftEngineColorUtils.fromHex(hex));
    }

    public static Item setColorFromRGB(Item item, String rgb) {
        return item.dyedColor(CraftEngineColorUtils.fromRGB(rgb));
    }
}