package net.cozyvanilla.cozylib.modules.mysql.interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerRepository<T> {
    @NotNull T getOrCreate(UUID uuid);
    void update(UUID uuid, T data);
    void prune();
}