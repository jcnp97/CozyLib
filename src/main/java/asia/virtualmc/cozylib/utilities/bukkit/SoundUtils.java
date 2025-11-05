package asia.virtualmc.cozylib.utilities.bukkit;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SoundUtils {
    private static final Map<String, Sound> cache = new HashMap<>();

    /**
     * Retrieves a {@link Sound} object based on the provided sound name, volume, and pitch.
     * Returns null if the name is invalid or cannot be parsed into a valid sound key.
     *
     * @param soundName the namespaced sound identifier (e.g., "minecraft:block.note_block.pling")
     * @param volume    the volume of the sound
     * @param pitch     the pitch of the sound
     * @return the constructed Sound object, or null if invalid
     */
    public static Sound get(String soundName, float volume, float pitch) {
        Sound sound = cache.get(soundName);
        if (sound != null) return sound;

        try {
            String[] parts = soundName.split(":", 2);
            String namespace = parts.length > 1 ? parts[0] : "minecraft";
            String key = parts.length > 1 ? parts[1] : parts[0];

            sound = Sound.sound()
                    .type(Key.key(namespace, key))
                    .source(Sound.Source.PLAYER)
                    .volume(volume)
                    .pitch(pitch)
                    .build();

            if (!namespace.equals("minecraft")) {
                cache.put(soundName, sound);
            }
            return sound;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Plays a sound for the specified player with the given name, volume, and pitch.
     * Does nothing if the player is offline or the sound name is empty.
     *
     * @param player    the target player
     * @param soundName the namespaced sound identifier
     * @param volume    the volume of the sound
     * @param pitch     the pitch of the sound
     */
    public static void playTo(@NotNull Player player, @NotNull String soundName, float volume, float pitch) {
        if (!player.isOnline() || soundName.isEmpty()) return;

        Sound sound = get(soundName, volume, pitch);
        if (sound != null) player.playSound(sound);
    }

    /**
     * Plays a sound for the specified player with default volume and pitch (1.0f).
     *
     * @param player    the target player
     * @param soundName the namespaced sound identifier
     */
    public static void playTo(Player player, String soundName) {
        playTo(player, soundName, 1, 1);
    }

    /**
     * Plays a sound at the specified location in the world.
     * Does nothing if the sound name or world is invalid.
     *
     * @param location  the location where the sound should play
     * @param soundName the namespaced sound identifier
     * @param volume    the volume of the sound
     * @param pitch     the pitch of the sound
     */
    public static void playAt(@NotNull Location location, @NotNull String soundName, float volume, float pitch) {
        Sound sound = get(soundName, volume, pitch);
        World world = location.getWorld();

        if (sound == null || world == null) return;
        world.playSound(sound, location.getX(), location.getY(), location.getZ());
    }

    /**
     * Plays a sound at the specified location with default volume and pitch (1.0f).
     *
     * @param location  the location where the sound should play
     * @param soundName the namespaced sound identifier
     */
    public static void playAt(@NotNull Location location, @NotNull String soundName) {
        playAt(location, soundName, 1, 1);
    }

    /**
     * Stops a previously played sound for the specified player.
     * Does nothing if the player is offline or the sound is not cached.
     *
     * @param player    the target player
     * @param soundName the namespaced sound identifier
     */
    public static void stop(Player player, String soundName) {
        if (player == null || !player.isOnline()) return;

        Sound sound = cache.get(soundName);
        if (sound != null) {
            player.stopSound(sound);
        }
    }
}
