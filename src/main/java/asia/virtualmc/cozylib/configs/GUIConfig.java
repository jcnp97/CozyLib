package asia.virtualmc.cozylib.configs;

import asia.virtualmc.cozylib.CozyLib;
import asia.virtualmc.cozylib.services.bukkit.ItemComponentWriter;
import asia.virtualmc.cozylib.services.files.YamlFileReader;
import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIConfig {
    private static final Map<String, String> unicodes = new HashMap<>();
    private static ItemModels itemModels;

    public record ItemModels(String invisible, String leftClick, String rightClick) {}

    /**
     * Loads GUI configuration values from "cozy-skills/guis.yml".
     * Clears and populates item models and menu unicodes.
     * Logs an error if loading fails.
     */
    public void load() {
        try {
            unicodes.clear();

            YamlFileReader.YamlFile file = YamlFileReader.get(CozyLib.getInstance(), "cozy-skills/guis.yml");
            itemModels = new ItemModels(
                    file.getString("item-models.invisible"),
                    file.getString("item-models.left-click"),
                    file.getString("item-models.right-click")
            );

            unicodes.putAll(file.stringKeyStringMap("menus", false));
        } catch (Exception e) {
            ConsoleUtils.severe("An error occurred when trying to load GUI configs: " + e);
        }
    }

    /**
     * Retrieves the ItemModels record.
     *
     * @return the record, else, null
     */
    public static ItemModels getModels() { return itemModels; }

    /**
     * Retrieves the configured invisible item model ID.
     *
     * @return the invisible model string, or a default if not set
     */
    public static String getInvisible() {
        String model = itemModels.invisible();
        return (model != null) ? model : "cozyvanilla_gui_items:invisible_item";
    }

    /**
     * Retrieves the configured left-click animation model ID.
     *
     * @return the left-click animation model string, or a default if not set
     */
    public static String getLeftClick() {
        String model = itemModels.leftClick();
        return (model != null) ? model : "cozyvanilla_gui_items:left_click_anim_menu";
    }

    /**
     * Retrieves the configured right-click animation model ID.
     *
     * @return the right-click animation model string, or a default if not set
     */
    public static String getRightClick() {
        String model = itemModels.rightClick();
        return (model != null) ? model : "cozyvanilla_gui_items:right_click_anim_menu";
    }

    /**
     * Creates and returns an invisible item using the configured model.
     *
     * @param name the display name of the item
     * @return an ItemStack representing the invisible item
     */
    public static ItemStack getInvisibleItem(String name) {
        return ItemComponentWriter.get(Material.PAPER, name, new ArrayList<>(), getInvisible());
    }

    /**
     * Creates and returns a left-click animation item using the configured model.
     *
     * @param name the display name of the item
     * @return an ItemStack representing the left-click animation item
     */
    public static ItemStack getLeftClickItem(String name) {
        return ItemComponentWriter.get(Material.PAPER, name, new ArrayList<>(), getLeftClick());
    }

    /**
     * Creates and returns a right-click animation item using the configured model.
     *
     * @param name the display name of the item
     * @return an ItemStack representing the right-click animation item
     */
    public static ItemStack getRightClickItem(String name) {
        return ItemComponentWriter.get(Material.PAPER, name, new ArrayList<>(), getRightClick());
    }

    /**
     * Retrieves the GUI title Unicode symbol or formatted title for a given menu name.
     *
     * @param title the menu name or key
     * @return the corresponding Unicode title, or an empty string if not found
     */
    public static String getMenu(String title) {
        String guiTitle = unicodes.get(title);
        if (guiTitle == null) {
            return "";
        }

        return guiTitle;
    }
}
