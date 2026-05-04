//package net.cozyvanilla.cozylib.util.bukkit;
//
//import org.bukkit.NamespacedKey;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class PlayerInventory {
//
//    public static byte[] serialize(org.bukkit.inventory.PlayerInventory inventory) {
//        return ItemStack.serializeItemsAsBytes(inventory.getContents());
//    }
//
//    public static ItemStack[] deserialize(byte[] bytes) {
//        return ItemStack.deserializeItemsFromBytes(bytes);
//    }
//
//    public static void setInventory(org.bukkit.inventory.PlayerInventory inventory, byte[] bytes) {
//        inventory.clear();
//        inventory.setContents(deserialize(bytes));
//    }
//
//    public static void setInventory(Player player, byte[] bytes) {
//        setInventory(player.getInventory(), bytes);
//    }
//
//    public static Map<Integer, ItemStack> getSnapshot(Player player, NamespacedKey key) {
//        Map<Integer, ItemStack> snapshot = new HashMap<>();
//        for (int i = 0; i < 36; i++) {
//            ItemStack item = player.getInventory().getItem(i);
//            if (item == null || !item.hasItemMeta()) continue;
//
////            if (PDCUtils.has(item, key)) {
////                snapshot.put(i, item.clone());
////            }
//        }
//
//        return snapshot;
//    }
//}
