package net.cozyvanilla.cozylib.common.models;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record CozyPlayer(UUID uuid, String name, Instant firstJoin, Instant lastJoin, Set<InetSocketAddress> addresses) {}