//package net.cozyvanilla.cozylib.modules.keyall_storage;
//
//import net.cozyvanilla.cozylib.modules.Module;
//import net.cozyvanilla.cozylib.modules.messages.Console;
//import net.cozyvanilla.cozylib.util.json.JsonFileReader;
//import net.cozyvanilla.cozylib.util.json.JsonFileWriter;
//import org.bukkit.plugin.Plugin;
//
//import java.io.File;
//import java.util.*;
//
//public final class KeyAllStorage implements Module<KeyAllStorageCommands> {
//    public final KeyAllStorageCommands commands;
//
//    private final Plugin plugin;
//    private final KeyAllStorageListener listener;
//    private final Map<UUID, List<String>> cache = new HashMap<>();
//
//    private JsonFileWriter writer;
//
//    public KeyAllStorage(Plugin plugin) {
//        this.plugin = plugin;
//        this.commands = new KeyAllStorageCommands(this);
//        this.listener = new KeyAllStorageListener(this);
//    }
//
//    @Override
//    public String getName() {
//        return "KeyAllStorage";
//    }
//
//    @Override
//    public String getPrefix() {
//        return "[CozyLib-" + getName() + "]";
//    }
//
//    @Override
//    public KeyAllStorageCommands getCommands() {
//        return commands;
//    }
//
//    @Override
//    public void getConfig() {}
//
//    @Override
//    public void enable() {
//        //getConfig();
//
//        JsonFileReader reader = new JsonFileReader(plugin, "modules/keyall_storage/storage.json");
//        File file = reader.getFile();
//        writer = new JsonFileWriter(plugin, file);
//
//        // read storage cache
//        for (String key : reader.getKeys("")) {
//            String itemName = reader.getString(key + ".item_name");
//            List<String> players = reader.getStringList(key + ".players");
//            if (itemName == null || players == null) {
//                Console.severe(getPrefix(), "Player list or item name is resulting to null! Please check `modules/keyall_storage/storage.json`.");
//                continue;
//            }
//
//            for (String s : players) {
//                UUID uuid = UUID.fromString(s);
//                cache.computeIfAbsent(uuid, k -> new ArrayList<>()).add(itemName);
//            }
//        }
//    }
//
//    @Override
//    public void disable() {}
//
//
//}