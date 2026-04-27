package net.cozyvanilla.cozylib.integrations.discordsrv;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.JDA;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiscordSRVUtil {
    private final JDA jda = DiscordSRV.getPlugin().getJda();

    public CompletableFuture<String> sendEmbedMessage(String channelId, EmbedBuilder embedBuilder) {
        CompletableFuture<String> future = new CompletableFuture<>();
        TextChannel channel = jda.getTextChannelById(channelId);

        if (channel == null) {
            future.completeExceptionally(new IllegalArgumentException("Channel not found"));
            return future;
        }

        channel.sendMessageEmbeds(embedBuilder.build()).queue(
                message -> future.complete(message.getId()),
                future::completeExceptionally
        );

        return future;
    }

    public void deleteMessage(String channelId, String messageId) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) return;

        channel.retrieveMessageById(messageId).queue(
                message -> message.delete().queue(),
                error -> {
                    // Do nothing if the message is not found or cannot be retrieved
                }
        );
    }

    public EmbedBuilder getEmbedMessage(String title,
                                               String description,
                                               Color color,
                                               List<String> fields,
                                               String footer) {
        EmbedBuilder embed = new EmbedBuilder();

        if (title != null && !title.isEmpty()) { embed.setTitle(title); }
        if (description != null && !description.isEmpty()) { embed.setDescription(description); }
        if (color != null) { embed.setColor(color); }
        if (footer != null && !footer.isEmpty()) { embed.setFooter(footer); }

        if (fields != null) {
            for (String field : fields) {
                if (field == null || field.isEmpty()) continue;

                String[] parts = field.split(";", 3);

                String name = parts.length > 0 ? parts[0] : null;
                String value = parts.length > 1 ? parts[1] : null;
                boolean inline = parts.length > 2 && Boolean.parseBoolean(parts[2]);

                if (name != null && value != null) {
                    embed.addField(name, value, inline);
                }
            }
        }

        return embed;
    }

    public String getTimestamp(Instant time) {
        if (time == null) {
            time = Instant.now();
        }

        return "<t:" + time.getEpochSecond() + ":R>";
    }
}
