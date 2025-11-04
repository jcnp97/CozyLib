package asia.virtualmc.cozylib;

import asia.virtualmc.cozylib.services.files.YamlFileReader;
import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Config {
    private static final Set<String> modules = new HashSet<>();
    private static MySQLDatabase database;
    private static ConsoleColors consoleColors;
    private static MessagePrefixes messagePrefixes;

    public record MySQLDatabase(String host, int port, String user, String pass, String dbName) {}
    public record ConsoleColors(String info, String warning, String severe) {}
    public record MessagePrefixes(String info, String notification, String warning, String severe, String broadcast) {}

    public Config() {
        YamlFileReader.YamlFile reader = YamlFileReader.get(CozyLib.getInstance(), "config.yml");

        try {
            // Modules
            Map<String, Boolean> modulesMap = reader.stringKeyBooleanMap("modules", false);
            for (Map.Entry<String, Boolean> entry : modulesMap.entrySet()) {
                if (entry.getValue()) {
                    modules.add(entry.getKey());
                }
            }

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

    public static boolean isEnabled(String moduleName) { return modules.contains(moduleName); }
    public static MySQLDatabase getDatabase() { return database; }
    public static ConsoleColors getConsoleColors() { return consoleColors; }
    public static MessagePrefixes getMessagePrefixes() { return messagePrefixes; }
}
