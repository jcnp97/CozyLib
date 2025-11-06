package asia.virtualmc.cozylib.utilities.bukkit.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemModelUtils {

    /**
     * Sets a custom item model on the given {@link ItemStack} using a namespaced key string.
     *
     * @param item      the item to modify
     * @param itemModel the resource location string of the model to apply
     */
    public static ItemStack set(ItemStack item, String itemModel) {
        if (item == null || itemModel == null || itemModel.isEmpty()) return item;

        NamespacedKey key = NamespacedKey.fromString(itemModel);
        if (key == null) return item;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setItemModel(key);
        item.setItemMeta(meta);
        return item;
    }


}
