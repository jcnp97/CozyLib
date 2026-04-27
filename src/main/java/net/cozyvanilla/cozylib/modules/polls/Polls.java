package net.cozyvanilla.cozylib.modules.polls;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.integrations.discordsrv.DiscordSRVAPI;
import net.cozyvanilla.cozylib.modules.Module;
import net.cozyvanilla.cozylib.services.files.JsonFileReader;
import net.cozyvanilla.cozylib.services.files.JsonFileWriter;
import net.cozyvanilla.cozylib.utilities.java.ColorUtils;
import net.cozyvanilla.cozylib.utilities.java.HashMapUtils;
import net.cozyvanilla.cozylib.utilities.numbers.DecimalUtils;
import net.cozyvanilla.cozylib.utilities.paper.AsyncUtils;
import net.cozyvanilla.cozylib.utilities.paper.TaskUtils;
import net.cozyvanilla.cozylib.utilities.string.StringUtils;
import net.cozyvanilla.cozylib.utilities.time.InstantUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.time.Instant;
import java.util.*;

public final class Polls implements Module<PollsCommands> {
    public final PollsCommands commands;

    private final Plugin plugin;

    private final Map<String, Double> polls = new LinkedHashMap<>();
    private final String channel = "1498210275013296199";

    private JsonFileWriter writer;
    private Instant expiresAt;
    private String lastMessageId;
    private ScheduledTask task;

    public Polls(Plugin plugin) {
        this.plugin = plugin;
        this.commands = new PollsCommands(this);
    }

    @Override
    public String getName() {
        return "Polls/CozyPolls";
    }

    @Override
    public String getPrefix() {
        return "[CozyPolls]";
    }

    @Override
    public PollsCommands getCommands() {
        return commands;
    }

    @Override
    public void enable() {
        //PollsAPI.register(this);

        JsonFileReader reader = new JsonFileReader(plugin, "modules/polls/storage.json");
        File file = reader.getFile();
        writer = new JsonFileWriter(plugin, file);

        // read cache
        expiresAt = InstantUtils.toInstant(reader.getString("expirestAt"));
        lastMessageId = reader.getString("lastMessageId");
        Map<String, Double> pollsFromFile = reader.getDoubleMap("polls");
        if (pollsFromFile != null) {
            polls.putAll(pollsFromFile);
        }

        // run task every 5 minutes
        task = TaskUtils.repeating(plugin, this::task, 300.0);
    }

    @Override
    public void disable() {
        if (task != null) {
            task.cancel();
        }
    }

    private String getProgress(double progress, int length) {
        char fill = '▰';
        char empty = '▱';

        progress = Math.max(0, Math.min(100, progress));
        int filledLength = (int) Math.round((progress / 100.0) * length);

        StringBuilder bar = new StringBuilder(length);
        for (int i = 0; i < filledLength; i++) {
            bar.append(fill);
        }

        for (int i = filledLength; i < length; i++) {
            bar.append(empty);
        }

        return bar.toString();
    }

    private Instant getExpiration(int days) {
        return InstantUtils.future((long) days * 24, Enums.TimeUnits.HOUR);
    }

    private double getSum() {
        double sum = 0;
        for (double value : polls.values()) {
            sum += value;
        }

        return sum;
    }

    private boolean hasEnded() {
        if (expiresAt == null) { return false; }
        return InstantUtils.hasExpired(expiresAt);
    }

    private List<String> generateFields(double sum, boolean hasEnded) {
        List<String> fields = new ArrayList<>();
        String timestamp = DiscordSRVAPI.getTimestamp(expiresAt);
        String winner = null;

        for (Map.Entry<String, Double> entry : polls.entrySet()) {
            String pollName = StringUtils.format(entry.getKey());
            double value = entry.getValue();

            double progress = DecimalUtils.format((value / sum) * 100.0);
            String bar = getProgress(progress, 12);
            fields.add(pollName + ";" + bar + " " + progress + "%;false");

            if (winner == null) {
                winner = pollName;
            }
        }

        if (hasEnded) {
            fields.add(";:party_popper: **Winner: " + winner + "**;false");
        } else {
            fields.add(";:hourglass: Ends In: " + timestamp + ";false");
        }

        return fields;
    }

    private EmbedBuilder createEmbed(boolean hasEnded) {
        double sum = getSum();
        String color = "#55FF55";
        HashMapUtils.sortDouble(polls, Enums.Ordering.DESC);

        if (hasEnded) {
            color = "#FF5555";
        }

        return DiscordSRVAPI.getEmbedMessage(
                "Which content would you like to see on the next update?",
                null,
                ColorUtils.fromHex(color),
                generateFields(sum, hasEnded),
                "Last Updated: " + InstantUtils.toReadable(Instant.now(), "Asia/Singapore"));
    }

    private void task() {
        if (expiresAt == null) { return; }
        boolean hasEnded = hasEnded();

        sendToDiscord(hasEnded);
        if (hasEnded) {
            expiresAt = null;
        }
    }

    // public
    public void sendToDiscord(boolean hasEnded) {
        // delete last post
        if (lastMessageId != null) {
            DiscordSRVAPI.deleteMessage(channel, lastMessageId);
        }

        // create new post
        DiscordSRVAPI.sendEmbedMessage(channel, createEmbed(hasEnded))
                .thenAccept(messageId -> {
                    lastMessageId = messageId;
                    AsyncUtils.async(plugin, () -> {
                        writer.writeString("lastMessageId", messageId, true);
                    });
                })
                .exceptionally(error -> {
                    error.printStackTrace();
                    return null;
                });
    }

    public void addPoll(String name) {
        polls.putIfAbsent(name, 0.0);
        AsyncUtils.async(plugin, () -> {
            writer.writeDouble("polls." + name, 0.0, false);
        });
    }

    public void startPoll(int days) {
        Instant expiration = getExpiration(days);
        String expirationStr = InstantUtils.toString(expiration);
        String id = UUID.randomUUID().toString();

        expiresAt = expiration;
        AsyncUtils.async(plugin, () -> {
            writer.writeString("expirestAt", expirationStr, false);
            writer.writeString("id", id, false);
        });
    }

    public void addExpiration(int days) {
        Instant expiration = getExpiration(days);
        String expirationStr = InstantUtils.toString(expiration);

        expiresAt = expiration;
        AsyncUtils.async(plugin, () -> {
            writer.writeString("expirestAt", expirationStr, true);
        });
    }

    public boolean addValue(String name, double amount) {
        Double value = polls.get(name);
        if (value != null) {
            value += amount;

            polls.put(name, value);
            Double finalValue = value;
            AsyncUtils.async(plugin, () -> {
                writer.writeDouble("polls." + name, finalValue, true);
            });

            return true;
        }

        return false;
    }

    public boolean hasStarted() { return expiresAt != null; }
    public boolean exists(String name) { return polls.get(name) != null; }
    public boolean isEmpty() { return polls.isEmpty(); }
}