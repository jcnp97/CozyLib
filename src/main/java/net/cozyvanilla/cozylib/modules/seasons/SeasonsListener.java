package net.cozyvanilla.cozylib.modules.seasons;

import net.cozyvanilla.cozylib.modules.seasons.events.SeasonsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

final class SeasonsListener implements Listener {
    private final Seasons seasons;

    SeasonsListener(Seasons seasons) {
        this.seasons = seasons;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        seasons.message(e.getPlayer());
    }

    @EventHandler
    public void onSeasonChange(SeasonsChangeEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            seasons.message(player);
        }
    }
}