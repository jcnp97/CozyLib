package asia.virtualmc.cozylib;

import asia.virtualmc.cozylib.utilities.bukkit.messages.ConsoleUtils;

import java.util.Map;

public class Registry {

    public Registry() {
        loadModules();
    }

    private void loadModules() {
        ConsoleUtils.info("Loaded modules:");
        for (Map.Entry<String, Boolean> entry : Config.getModules().entrySet()) {
            String module = entry.getKey();
            boolean isEnabled = entry.getValue();

            switch (module) {
                case "sqlite" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- SQLite");
                    } else {
                        ConsoleUtils.severe("- SQLite");
                    }
                }

                case "mysql" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- MySQL");
                    } else {
                        ConsoleUtils.severe("- MySQL");
                    }
                }

                case "worldguard" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- WorldGuard");
                    } else {
                        ConsoleUtils.severe("- WorldGuard");
                    }
                }

                case "vault" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- Vault");
                    } else {
                        ConsoleUtils.severe("- Vault");
                    }
                }

                case "realistic_seasons" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- RealisticSeasons");
                    } else {
                        ConsoleUtils.severe("- RealisticSeasons");
                    }
                }

                case "skins_restorer" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- SkinsRestorer");
                    } else {
                        ConsoleUtils.severe("- SkinsRestorer");
                    }
                }

                case "better_model" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- BetterModel");
                    } else {
                        ConsoleUtils.severe("- BetterModel");
                    }
                }

                case "hologram_lib" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- HologramLib");
                    } else {
                        ConsoleUtils.severe("- HologramLib");
                    }
                }

                case "ultimate_advancement" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- UltimateAdvancement");
                    } else {
                        ConsoleUtils.severe("- UltimateAdvancement");
                    }
                }

                case "ray_trace" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- RayTracing");
                    } else {
                        ConsoleUtils.severe("- RayTracing");
                    }
                }

                case "uni_dialogs" -> {
                    if (isEnabled) {
                        ConsoleUtils.info("- UniDialogs");
                    } else {
                        ConsoleUtils.severe("- UniDialogs");
                    }
                }
            }
        }

    }
}
