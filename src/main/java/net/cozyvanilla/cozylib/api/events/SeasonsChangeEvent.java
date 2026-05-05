package net.cozyvanilla.cozylib.api.events;

import net.cozyvanilla.cozylib.common.enums.SeasonType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SeasonsChangeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final SeasonType season;

    public SeasonsChangeEvent(SeasonType season) {
        this.season = season;
    }

    public SeasonType getSeason() {
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
