package asia.virtualmc.cozylib.utilities.misc;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * A reusable Caffeine cache that supports a custom TTL for every put/add.
 * TTL is applied only on write (create/update). Reads do not affect expiry.
 */
public final class CacheUtils<K, V> {
    private final Cache<K, Holder<V>> cache;

    /**
     * Wraps a value with its TTL in nanoseconds (decided at write-time).
     */
    private static final class Holder<V> {
        final V value;
        final long ttlNanos;

        Holder(V value, long ttlMillis) {
            this.value = Objects.requireNonNull(value, "value");
            if (ttlMillis < 0) {
                throw new IllegalArgumentException("ttlMillis must be >= 0");
            }
            this.ttlNanos = TimeUnit.MILLISECONDS.toNanos(ttlMillis);
        }
    }

    /**
     * Build a variable-TTL cache.
     *
     * @param maximumSize max entries to keep (use Long.MAX_VALUE or remove if you don’t want a size cap)
     */
    public CacheUtils(long maximumSize) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                // Per-entry expiry where only writes control the TTL
                .expireAfter(new Expiry<K, Holder<V>>() {
                    @Override
                    public long expireAfterCreate(K key, Holder<V> value, long currentTime) {
                        return value.ttlNanos; // set at put-time
                    }

                    @Override
                    public long expireAfterUpdate(K key, Holder<V> value, long currentTime, long currentDuration) {
                        return value.ttlNanos; // refresh to the new write-time TTL
                    }

                    @Override
                    public long expireAfterRead(K key, Holder<V> value, long currentTime, long currentDuration) {
                        return currentDuration; // do NOT change on read (expire-after-write only)
                    }
                })
                .build();
    }

    /**
     * Put a value with a custom TTL (in milliseconds). Overwrites existing value & TTL.
     */
    public void put(K key, V value, long ttlMillis) {
        cache.put(key, new Holder<>(value, ttlMillis));
    }

    /**
     * Compute value if absent, then store it with the given TTL.
     * If present, replaces TTL (and value if you return something different).
     */
    public V compute(K key, Function<? super K, ? extends V> mappingFn, long ttlMillis) {
        Holder<V> result = cache.asMap().compute(key, (k, existing) -> {
            V v = (existing != null) ? existing.value : mappingFn.apply(k);
            return new Holder<>(v, ttlMillis);
        });
        return result.value;
    }

    /**
     * Get the cached value (or null if absent/expired).
     */
    public V getIfPresent(K key) {
        Holder<V> h = cache.getIfPresent(key);
        return (h == null) ? null : h.value;
    }

    /**
     * Remove an entry.
     */
    public void invalidate(K key) {
        cache.invalidate(key);
    }

    /**
     * Clear all entries.
     */
    public void invalidateAll() {
        cache.invalidateAll();
    }
}
