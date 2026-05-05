package net.cozyvanilla.cozylib.modules.mysql.interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerRepository<T> {

    @NotNull Optional<T> get(UUID uuid);
    @NotNull T create(UUID uuid);
    void update(UUID uuid, @NotNull T data);

    @NotNull CompletableFuture<Optional<T>> getAsync(UUID uuid);
    @NotNull CompletableFuture<T> createAsync(UUID uuid);
    @NotNull CompletableFuture<Void> updateAsync(UUID uuid, @NotNull T data);
}