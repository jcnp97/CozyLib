package net.cozyvanilla.cozylib.utilities.bukkit;

import net.cozyvanilla.cozylib.modules.messages.Console;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundUtils {

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
        try {
            String[] parts = soundName.split(":", 2);
            String namespace = parts.length > 1 ? parts[0] : "minecraft";
            String key = parts.length > 1 ? parts[1] : parts[0];

            return Sound.sound()
                    .type(Key.key(namespace, key))
                    .source(Sound.Source.PLAYER)
                    .volume(volume)
                    .pitch(pitch)
                    .build();
        } catch (Exception e) {
            Console.severe(soundName + " is not a valid sound!");
        }

        return null;
    }

    /**
     * Gets a sound instance using the given sound name with default volume and pitch of 1.0.
     *
     * @param soundName the sound name to resolve
     * @return the resolved sound, or null if invalid
     */
    public static Sound get(String soundName) {
        return get(soundName, 1.0f, 1.0f);
    }

    /**
     * Plays a sound to the specified player using the given volume and pitch.
     *
     * @param player the player to play the sound to
     * @param soundName the sound name to play
     * @param volume the sound volume
     * @param pitch the sound pitch
     */
    public static void playTo(@NotNull Player player, String soundName, float volume, float pitch) {
        if (!player.isOnline() || soundName.isEmpty()) return;

        Sound sound = get(soundName, volume, pitch);
        if (sound != null) player.playSound(sound);
    }

    /**
     * Plays a sound to the specified player with default volume and pitch of 1.0.
     *
     * @param player the player to play the sound to
     * @param soundName the sound name to play
     */
    public static void playTo(@NotNull Player player, String soundName) {
        playTo(player, soundName, 1.0f, 1.0f);
    }

    /**
     * Plays a sound at the specified location using the given volume and pitch.
     *
     * @param location the location where the sound will be played
     * @param soundName the sound name to play
     * @param volume the sound volume
     * @param pitch the sound pitch
     */
    public static void playAt(@NotNull Location location, @NotNull String soundName, float volume, float pitch) {
        Sound sound = get(soundName, volume, pitch);
        World world = location.getWorld();

        if (sound == null || world == null) return;
        world.playSound(sound, location.getX(), location.getY(), location.getZ());
    }

    /**
     * Plays a sound at the specified location with default volume and pitch of 1.0.
     *
     * @param location the location where the sound will be played
     * @param soundName the sound name to play
     */
    public static void playAt(@NotNull Location location, @NotNull String soundName) {
        playAt(location, soundName, 1, 1);
    }

    /**
     * Stops the specified sound for the given player.
     *
     * @param player the player to stop the sound for
     * @param soundName the sound name to stop
     */
    public static void stop(@NotNull Player player, String soundName) {
        if (!player.isOnline()) return;

        Sound sound = get(soundName, 1.0f, 1.0f);
        if (sound != null) {
            player.stopSound(sound);
        }
    }

    /**
     * Plays a sound to all players on the server using the given volume and pitch.
     *
     * @param soundName the sound name to play
     * @param volume the sound volume
     * @param pitch the sound pitch
     */
    public static void playToAll(String soundName, float volume, float pitch) {
        Sound sound = get(soundName, volume, pitch);
        if (sound != null) Bukkit.getServer().playSound(sound);
    }

    /**
     * Plays a sound to all players on the server with default volume and pitch of 1.0.
     *
     * @param soundName the sound name to play
     */
    public static void playToAll(String soundName) {
        playToAll(soundName, 1.0f, 1.0f);
    }
}
