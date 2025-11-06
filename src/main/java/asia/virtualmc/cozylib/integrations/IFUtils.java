package asia.virtualmc.cozylib.integrations;

import asia.virtualmc.cozylib.CozyLib;
import asia.virtualmc.cozylib.utilities.bukkit.items.ItemStackUtils;
import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;
import asia.virtualmc.cozylib.utilities.paper.TaskUtils;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.PagingButtons;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class IFUtils {
    private static final Set<UUID> responseCache = ConcurrentHashMap.newKeySet();

    public static ChestGui getGui(String title, String soundId, int rows) {
        ChestGui gui = new ChestGui(rows, title);
        gui.setOnGlobalClick(event -> {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            Player player = (Player) event.getWhoClicked();
            player.playSound(player, soundId != null ? soundId : "minecraft:ui.button.click", 1, 1);
        });

        return gui;
    }

    public static void addClickableButton(
            ChestGui gui,
            String title,
            List<String> lore,
            String buttonModel,
            int slot,
            Runnable task,
            boolean closeOnClick
    ) {
        StaticPane staticPane = new StaticPane(9, 6);
        ItemStack invisible = ItemStackUtils.create(Material.PAPER, title, lore, "cozyvanilla_gui_items:invisible_item");
        ItemStack normal = ItemStackUtils.create(Material.PAPER, title, lore, buttonModel);
        ItemStack clicked = ItemStackUtils.create(Material.PAPER, title, lore, buttonModel + "_clicked");

        int dummySlot = slot - 1;
        int rightHalfSlot = slot + 1;

        GuiItem leftBH = new GuiItem(invisible);
        GuiItem rightBH = new GuiItem(invisible);

        staticPane.addItem(new GuiItem(normal), Slot.fromIndex(dummySlot)); // dummy
        staticPane.addItem(leftBH, Slot.fromIndex(slot));
        staticPane.addItem(rightBH, Slot.fromIndex(rightHalfSlot));

        Consumer<InventoryClickEvent> clickAction = event -> {
            staticPane.removeItem(Slot.fromIndex(dummySlot));
            staticPane.addItem(new GuiItem(clicked), Slot.fromIndex(dummySlot));
            gui.update();

            TaskUtils.delay(CozyLib.getInstance(), () -> {
                if (closeOnClick) {
                    event.getWhoClicked().closeInventory();
                    return;
                }

                staticPane.removeItem(Slot.fromIndex(dummySlot));
                staticPane.addItem(new GuiItem(normal), Slot.fromIndex(dummySlot));
                task.run();
                gui.update();
            }, 10L);
        };

        leftBH.setAction(clickAction);
        rightBH.setAction(clickAction);

        gui.addPane(staticPane);
    }

    public static void confirmGui(Player player, Consumer<Boolean> callback) {
        UUID uuid = player.getUniqueId();
        responseCache.remove(uuid);

        ChestGui gui = new ChestGui(1, GUIConfig.getMenu("confirmation_menu"));
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane pane = new StaticPane(0, 0, 9, 1);

        // Confirm buttons
        ItemStack confirmButton = GUIConfig.getLeftClickItem("<green>ᴄᴏɴғɪʀᴍ ᴀᴄᴛɪᴏɴ");
        GuiItem confirm = new GuiItem(confirmButton, event -> {
            callback.accept(true);
            event.getWhoClicked().closeInventory();
        });
        pane.addItem(confirm, Slot.fromIndex(2));

        // Cancel buttons
        ItemStack cancelButton = GUIConfig.getLeftClickItem("<red>ᴄᴀɴᴄᴇʟ ᴀᴄᴛɪᴏɴ");
        GuiItem cancel = new GuiItem(cancelButton, event -> {
            callback.accept(false);
            event.getWhoClicked().closeInventory();
        });
        pane.addItem(cancel, Slot.fromIndex(6));

        gui.setOnClose(event -> {
            if (responseCache.add(uuid)) {
                callback.accept(false);
            }
        });

        gui.addPane(pane);
        gui.show(player);
    }

    public static ChestGui getDisplayGui(String title, List<ItemStack> items,
                                         GuiItem prevButton, GuiItem nextButton) {
        ChestGui gui = new ChestGui(6, title);
        PaginatedPane pane = new PaginatedPane(0, 0, 9, 5);

        List<GuiItem> content = new ArrayList<>();
        for (ItemStack item : items) {
            content.add(new GuiItem(item));
        }

        pane.populateWithGuiItems(content);

        PagingButtons pagingButtons = new PagingButtons(Slot.fromXY(0, 5), 9, pane);
        pagingButtons.setBackwardButton(prevButton);
        pagingButtons.setForwardButton(nextButton);
        pagingButtons.setOnClick(event -> {
            Player player = (Player) event.getWhoClicked();
            player.playSound(player, "minecraft:ui.button.click", 1, 1);
        });

        gui.addPane(pane);
        gui.addPane(pagingButtons);
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        return gui;
    }

    public static void inputGui(@NotNull Player player, String title, Consumer<String> onConfirm) {
        AnvilGui gui = new AnvilGui(title);

        ItemStack confirmItem = ItemStackUtils.create(Material.LIME_CONCRETE, "✔ Confirm", null, "");
        GuiItem confirmButton = new GuiItem(confirmItem, event -> {
            event.setCancelled(true);
            String input = gui.getRenameText();
            try {
                onConfirm.accept(input);
            } catch (Throwable t) {
                ConsoleUtils.severe("Exception in anvil input callback (title='" + title + "', player=" + player.getName() + "): " + t);
            } finally {
                event.getWhoClicked().closeInventory();
            }
        });

        gui.getResultComponent().setItem(confirmButton, 0, 0);

        gui.setOnGlobalClick(e -> e.setCancelled(true));
        gui.setOnBottomClick(e -> e.setCancelled(true));
        gui.setOnTopClick(e -> e.setCancelled(true));
        gui.setOnClose(e -> {
            onConfirm.accept("");
        });

        gui.setCost((short) 0);
        gui.show(player);
    }
}
