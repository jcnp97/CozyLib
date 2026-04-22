package net.cozyvanilla.cozylib.utilities.java;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.Policy;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * A cache utility that supports per-entry expiration using Caffeine's {@link Expiry} policy.
 *
 * <p>Each entry can have its own TTL (time-to-live), enabling fine-grained control over
 * cache eviction without requiring separate cache instances per expiration rule.
 *
 * <p>Usage example:
 * <pre>{@code
 * PerEntryExpiryCache<String, String> cache = PerEntryExpiryCache.<String, String>builder()
 *     .maximumSize(500)
 *     .defaultTtl(Duration.ofMinutes(10))
 *     .build();
 *
 * cache.put("session:user1", "token_abc", Duration.ofMinutes(30));
 * cache.put("config:timeout", "5000", Duration.ofHours(1));
 *
 * Optional<String> value = cache.get("session:user1");
 * }</pre>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public final class PerEntryExpiryCache<K, V> {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    /**
     * The cache is configured for variable expiration. The Expiry is only a fallback
     * for plain cache.put(key, value) calls. For explicit per-entry TTL writes, use
     * put(key, value, ttl), which delegates to Policy.VarExpiration.put(...).
     */
    private final Cache<K, V> cache;
    private final Policy.VarExpiration<K, V> varExpiration;

    private PerEntryExpiryCache(Cache<K, V> cache,
                                Function<? super V, Duration> defaultTtlFunction) {
        this.cache = cache;
        this.varExpiration = cache.policy()
                .expireVariably()
                .orElseThrow(() -> new IllegalStateException(
                        "Cache was not built with variable expiration"));
    }

    /**
     * Creates a variable-expiration cache.
     *
     * @param maximumSize optional max size
     * @param defaultTtlFunction fallback TTL used when caller uses put(key, value)
     */
    public static <K, V> PerEntryExpiryCache<K, V> create(
            long maximumSize,
            Function<? super V, Duration> defaultTtlFunction) {

        Objects.requireNonNull(defaultTtlFunction, "defaultTtlFunction must not be null");

        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfter(Expiry.creating((K key, V value) ->
                        sanitizeTtl(defaultTtlFunction.apply(value))))
                .build();

        return new PerEntryExpiryCache<>(cache, defaultTtlFunction);
    }

    /**
     * Writes an entry with an explicit TTL.
     */
    public void put(K key, V value, Duration ttl) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(ttl, "ttl must not be null");
        varExpiration.put(key, value, sanitizeTtl(ttl));
    }

    /**
     * Writes an entry using the default TTL function.
     */
    public void put(K key, V value) {
        Objects.requireNonNull(key, "key must not be null");
        cache.put(key, value);
    }

    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }

    public V get(K key, Function<? super K, ? extends V> mappingFunction) {
        return cache.get(key, mappingFunction);
    }

    public Optional<V> getOptional(K key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    public boolean containsKey(K key) {
        return cache.getIfPresent(key) != null;
    }

    public void invalidate(K key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public long estimatedSize() {
        return cache.estimatedSize();
    }

    public ConcurrentMap<K, V> asMap() {
        return cache.asMap();
    }

    public Map<K, V> snapshot() {
        return Map.copyOf(cache.asMap());
    }

    /**
     * Updates the expiration time of an existing entry.
     */
    public void setExpiresAfter(K key, Duration ttl) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(ttl, "ttl must not be null");
        varExpiration.setExpiresAfter(key, sanitizeTtl(ttl));
    }

    /**
     * Reads the remaining time to live for an entry when available.
     */
    public Optional<Duration> expiresAfter(K key) {
        return varExpiration.getExpiresAfter(key);
    }

    private static Duration sanitizeTtl(Duration ttl) {
        if (ttl == null) {
            return DEFAULT_TTL;
        }
        if (ttl.isNegative() || ttl.isZero()) {
            return Duration.ofNanos(1); // expire essentially immediately
        }
        return ttl;
    }
}
