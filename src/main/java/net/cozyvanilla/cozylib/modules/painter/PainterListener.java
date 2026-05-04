//package net.cozyvanilla.cozylib.modules.painter;
//
//import net.cozyvanilla.cozylib.core.util.Cooldown;
//import net.momirealms.craftengine.bukkit.api.event.FurnitureInteractEvent;
//import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurniture;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//
//import java.util.UUID;
//
//final class PainterListener implements Listener {
//    private final Painter painter;
//
//    PainterListener(Painter painter) {
//        this.painter = painter;
//    }
//
//    @EventHandler
//    public void onFurnitureInteract(FurnitureInteractEvent e) {
//        // cooldown check
//        Player player = e.getPlayer();
//        UUID uuid = player.getUniqueId();
//        if (Cooldown.getOrStart(this.getClass(), uuid, "painting", 1) != 0) return;
//
//        // retrieve mainhand item
//
//
//        BukkitFurniture furniture = e.furniture();
//    }
//}