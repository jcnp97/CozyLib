package asia.virtualmc.cozylib;

import asia.virtualmc.cozylib.configs.GUIConfig;
import asia.virtualmc.cozylib.configs.GlyphsConfig;
import asia.virtualmc.cozylib.services.files.YamlFileReader;
import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;
import asia.virtualmc.cozylib.utilities.paper.AsyncUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Config {
    private final GlyphsConfig glyphsConfig;
    private final GUIConfig guiConfig;

    private static final Map<String, Boolean> modules = new HashMap<>();
    private static MySQLDatabase database;
    private static ConsoleColors consoleColors;
    private static MessagePrefixes messagePrefixes;

    public record MySQLDatabase(String host, int port, String user, String pass, String dbName) {}
    public record ConsoleColors(String info, String warning, String severe) {}
    public record MessagePrefixes(String info, String notification, String warning, String severe, String broadcast) {}

    public Config() {
        this.glyphsConfig = new GlyphsConfig();
        this.guiConfig = new GUIConfig();
        load();
    }

    public boolean load() {
        try {
            read();
            glyphsConfig.load();
            guiConfig.load();

            return true;
        } catch (Exception e) {
            ConsoleUtils.severe("An error occurred when trying to read configs: " + e);
        }

        return false;
    }

    private void read() {
        YamlFileReader.YamlFile reader = YamlFileReader.get(CozyLib.getInstance(), "config.yml");
        try {
            // Modules
            modules.putAll(reader.stringKeyBooleanMap("modules", false));

            // MySQL Database
            database = new MySQLDatabase(
                    reader.getString("mysql.host"),
                    reader.getInt("mysql.port"),
                    reader.getString("mysql.username"),
                    reader.getString("mysql.password"),
                    reader.getString("mysql.database"));

            // Console-Coded Console Messages
            consoleColors = new ConsoleColors(
                    reader.getString("console-colors.info"),
                    reader.getString("console-colors.warning"),
                    reader.getString("console-colors.severe"));

            // Message Prefixes
            messagePrefixes = new MessagePrefixes(
                    reader.getString("message-prefixes.info"),
                    reader.getString("message-prefixes.notification"),
                    reader.getString("message-prefixes.warning"),
                    reader.getString("message-prefixes.severe"),
                    reader.getString("message-prefixes.broadcast"));
        } catch (Exception e) {
            ConsoleUtils.severe("Unable to read config.yml: " + e);
        }
    }

    public void reload() {
        ConsoleUtils.info("Reloading.. Please wait..");
        AsyncUtils.runAsyncThenSync(CozyLib.getInstance(), this::load, (result) -> {
            if (result) {
                ConsoleUtils.info("Successfully reloaded configs!");
            }
        });
    }

    public static Map<String, Boolean> getModules() { return modules; }
    public static MySQLDatabase getDatabase() { return database; }
    public static ConsoleColors getConsoleColors() { return consoleColors; }
    public static MessagePrefixes getMessagePrefixes() { return messagePrefixes; }
}
