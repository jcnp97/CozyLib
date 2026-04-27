package net.cozyvanilla.cozylib.integrations.craftengine;

import net.cozyvanilla.cozylib.modules.messages.Console;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.item.BukkitItemDefinition;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.util.Color;
import org.bukkit.inventory.ItemStack;
import net.momirealms.craftengine.core.util.Key;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CraftEngineItem {

    public CraftEngineItem() {}

    private ItemStack getItemStack(Key key) {
        BukkitItemDefinition item = CraftEngineItems.byId(key);
        if (item == null) {
            Console.severe("Invalid CraftEngineItem id '" + key + "'");
            return null;
        }

        return item.buildBukkitItem();
    }

    private boolean isCraftEngineItem(ItemStack itemStack) {
        return CraftEngineItems.getCustomItemId(itemStack) != null;
    }

    private Item setColor(Item item, Color color) {
        return item.dyedColor(color);
    }

    // public API
    @Nullable
    public ItemStack getItemStack(String namespace, String itemName) {
        return getItemStack(CraftEngine.util().toKey(namespace, itemName));
    }

    @Nullable
    public ItemStack getItemStack(String id) {
        return getItemStack(CraftEngine.util().toKey(id));
    }

    @Nullable
    public Key getKey(ItemStack itemStack) {
        return CraftEngineItems.getCustomItemId(itemStack);
    }

    @Nullable
    public String getItemId(@NotNull ItemStack itemStack) {
        Key key = getKey(itemStack);
        return key != null ? key.toString() : null;
    }

    public Item setColorFromHex(Item item, String hex) {
        return item.dyedColor(CraftEngine.util().fromHex(hex));
    }

    public Item setColorFromRGB(Item item, String rgb) {
        return item.dyedColor(CraftEngine.util().fromRGB(rgb));
    }
}