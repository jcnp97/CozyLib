package net.cozyvanilla.cozylib.modules.seasons;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.modules.Module;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.modules.messages.Messages;
import net.cozyvanilla.cozylib.modules.seasons.events.SeasonsChangeEvent;
import net.cozyvanilla.cozylib.services.files.JsonFileReader;
import net.cozyvanilla.cozylib.services.files.JsonFileWriter;
import net.cozyvanilla.cozylib.services.files.YamlFileReader;
import net.cozyvanilla.cozylib.utilities.paper.TaskUtils;
import net.cozyvanilla.cozylib.utilities.time.InstantUtils;
import net.cozyvanilla.cozylib.utilities.time.LocalDateUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public final class Seasons implements Module<SeasonsCommands> {
    public final SeasonsCommands commands;

    private final Plugin plugin;
    private final SeasonsListener listener;

    private ScheduledTask task;
    private String timeZone;
    private Instant started;
    private Enums.Seasons currentSeason;
    private String springMessage;
    private String summerMessage;
    private String fallMessage;
    private String winterMessage;

    private final String channel = "cozyseasons:season";
    private final Map<Integer, Enums.Seasons> seasonSchedule = new HashMap<>();

    public Seasons(Plugin plugin) {
        this.plugin = plugin;
        this.listener = new SeasonsListener(this);
        this.commands = new SeasonsCommands(this);
    }

    @Override
    public String getName() {
        return "Seasons/CozySeasons";
    }

    @Override
    public String getPrefix() {
        return "[CozySeasons]";
    }

    @Override
    public SeasonsCommands getCommands() {
        return commands;
    }

    @Override
    public void enable() {
        SeasonsAPI.register(this);

        // Read configs
        YamlFileReader config = new YamlFileReader(plugin, "modules/seasons.yml");
        timeZone = config.get().getString("time_zone");
        springMessage = config.get().getString("messages.spring");
        summerMessage = config.get().getString("messages.summer");
        fallMessage = config.get().getString("messages.fall");
        winterMessage = config.get().getString("messages.winter");

        Map<Integer, String> schedule = config.intKeyStringMap("schedule");
        for (Map.Entry<Integer, String> entry : schedule.entrySet()) {
            seasonSchedule.put(entry.getKey(), Enums.Seasons.valueOf(entry.getValue()));
        }

        // Write into seasons.json, does not overwrite
        JsonFileWriter writer = new JsonFileWriter(plugin, "caches/seasons.json");
        Instant now = InstantUtils.now();
        writer.writeString("date_started", InstantUtils.toString(now), false);

        // Load data into cache and update season
        JsonFileReader reader = new JsonFileReader(plugin, writer.getFile());
        String dateStarted = reader.getString("date_started");
        started = Instant.parse(dateStarted);
        currentSeason = calculateSeason();

        // Setup plugin channel for mod client
        getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);

        // Schedule repeating task every 1 minute
        task = TaskUtils.repeating(plugin, this::run, 1200L);

        // Enable event listener
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void disable() {
        if (task != null) task.cancel();
        getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, channel);
        SeasonsAPI.unregister(this);
    }

    public void set(Enums.Seasons season) {
        currentSeason = season;
        Bukkit.getPluginManager().callEvent(new SeasonsChangeEvent(currentSeason));
    }

    public void reset() {
        JsonFileWriter writer = new JsonFileWriter(plugin, "caches/seasons.json");
        String dateStarted = InstantUtils.toString(InstantUtils.now());
        writer.writeString("date_started", dateStarted, true);

        started = Instant.parse(dateStarted);
        currentSeason = calculateSeason();
    }

    public void message(Player player) {
        Messages.message(player, getMessage(currentSeason), Enums.MessageType.NOTIFY);
        modPacket(player, currentSeason.toString().toLowerCase());
    }

    public Enums.Seasons getCurrentSeason() {
        return currentSeason;
    }

    // PRIVATE METHODS
    private Enums.Seasons calculateSeason() {
        long daysSince = LocalDateUtils.daysSince(started, timeZone);
        if (daysSince >= 28) {
            daysSince = daysSince % 27;
        }

        return seasonSchedule.get((int) daysSince);
    }

    private void run() {
        Enums.Seasons newSeason = calculateSeason();
        if (newSeason != currentSeason) {
            set(newSeason);
        }
    }

    private String getMessage(Enums.Seasons season) {
        return switch (season) {
            case SPRING -> springMessage;
            case SUMMER -> summerMessage;
            case FALL -> fallMessage;
            case WINTER -> winterMessage;
            case DISABLED -> "";
        };
    }

    private void modPacket(Player player, String season) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            byte[] strBytes = season.getBytes(StandardCharsets.UTF_8);
            writeVarInt(out, strBytes.length);
            out.write(strBytes);
            player.sendPluginMessage(plugin, channel, bytes.toByteArray());
        } catch (IOException e) {
            Console.severe(getPrefix(), "Failed to send season packet to mod client: " + e.getMessage());
        }
    }

    private void writeVarInt(DataOutputStream out, int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value);
    }
}