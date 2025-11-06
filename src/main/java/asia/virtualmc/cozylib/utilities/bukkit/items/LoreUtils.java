package asia.virtualmc.cozylib.utilities.bukkit.items;

import asia.virtualmc.cozylib.utilities.bukkit.messages.AdventureUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoreUtils {

    /**
     * Appends a list of lore lines to an item at a specified index position.
     *
     * @param item      the {@link ItemStack} to modify
     * @param toAppend  the list of lore strings to append
     * @param index     the position to insert the lore; -2 or 0 = top, -1 or >= size = bottom
     * @return the modified cloned {@link ItemStack}, or null if the item is null
     */
    public static ItemStack append(ItemStack item, List<String> toAppend, int index) {
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        List<Component> lore = meta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        } else {
            lore = new ArrayList<>(lore);
        }

        if (toAppend != null && !toAppend.isEmpty()) {
            List<Component> newLore = AdventureUtils.toComponent(toAppend);
            if (index <= -2) {
                lore.addAll(0, newLore);
            } else if (index == -1) {
                lore.addAll(newLore);
            } else if (index == 0) {
                lore.addAll(0, newLore);
            } else if (index >= lore.size()) {
                lore.addAll(newLore);
            } else {
                lore.addAll(index, newLore);
            }
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        return item.clone();
    }

    /**
     * Appends a single lore line to an item at a specified index position.
     *
     * @param item      the {@link ItemStack} to modify
     * @param toAppend  the single lore string to append
     * @param index     the position to insert the lore; -2 or 0 = top, -1 or >= size = bottom
     * @return the modified cloned {@link ItemStack}, or null if the item is null
     */
    public static ItemStack append(ItemStack item, String toAppend, int index) {
        return append(item, Collections.singletonList(toAppend), index);
    }

    /**
     * Replaces the entire lore of an item with a new list of lore lines.
     *
     * @param item       the {@link ItemStack} to modify
     * @param toReplace  the new list of lore strings to set
     * @return the modified cloned {@link ItemStack}, or null if the item is null
     */
    public static ItemStack replace(ItemStack item, List<String> toReplace) {
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        List<Component> newLore = null;

        if (toReplace != null && !toReplace.isEmpty()) {
            newLore = AdventureUtils.toComponent(toReplace);
        }

        meta.lore(newLore);
        item.setItemMeta(meta);

        return item.clone();
    }

    /**
     * Replaces the entire lore of an item with a single lore line.
     *
     * @param item       the {@link ItemStack} to modify
     * @param toReplace  the single lore string to set
     * @return the modified cloned {@link ItemStack}, or null if the item is null
     */
    public static ItemStack replace(ItemStack item, String toReplace) {
        return replace(item, Collections.singletonList(toReplace));
    }
}
