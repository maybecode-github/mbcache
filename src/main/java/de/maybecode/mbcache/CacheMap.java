package de.maybecode.mbcache;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheMap<K, V> {

    void cache(K key, V value);

    void cache(K key, V value, long expireAfter, TimeUnit expirationTimeUnit);

    void unCache(K key);

    void reloadCache(K key, V value);

    void reloadCache(K key, V value, long expireAfter, TimeUnit expirationTimeUnit);

    Optional<V> findByKey(K key);

    Optional<K> findByValue(V value);

    List<K> findKeys();

    List<V> findValues();

    boolean isCached(K key);

    boolean isCachedValue(V value);

    /**
     * clears cache completely after time
     *
     * @param amount   amount of time after cache gets cleared
     * @param timeUnit unit of time after cache gets cleared
     */
    void expireAfter(long amount, TimeUnit timeUnit);

}
