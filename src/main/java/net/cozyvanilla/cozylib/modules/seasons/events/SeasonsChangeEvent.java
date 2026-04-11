package net.cozyvanilla.cozylib.modules.seasons.events;

import net.cozyvanilla.cozylib.Enums;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SeasonsChangeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Enums.Seasons season;

    public SeasonsChangeEvent(Enums.Seasons season) {
        this.season = season;
    }

    public Enums.Seasons getSeason() {
        return season;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
