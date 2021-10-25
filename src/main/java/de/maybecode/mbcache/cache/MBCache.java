package de.maybecode.mbcache.cache;

import de.maybecode.mbcache.CacheMap;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MBCache<K, V> implements CacheMap<K, V> {

    private final Map<K, V> cacheMap = new HashMap<>();

    @Override
    public void cache(K key, V value) {
        if (!cacheMap.containsKey(key))
            cacheMap.put(key, value);
    }

    @Override
    public void cache(K key, V value, long expireAfter, TimeUnit expirationTimeUnit) {
        cache(key, value);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable expiration = () -> reloadCache(key, value, expireAfter, expirationTimeUnit);
        executorService.schedule(expiration, expireAfter, expirationTimeUnit);
        executorService.shutdown();
    }

    @Override
    public void unCache(K key) {
        cacheMap.remove(key);
    }

    @Override
    public void reloadCache(K key, V value) {
        cacheMap.replace(key, value);
    }

    @Override
    public void reloadCache(K key, V value, long expireAfter, TimeUnit expirationTimeUnit) {
        reloadCache(key, value);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable expiration = () -> reloadCache(key, value, expireAfter, expirationTimeUnit);
        executorService.schedule(expiration, expireAfter, expirationTimeUnit);
        executorService.shutdown();
    }

    @Override
    public Optional<V> findByKey(K key) {
        return Optional.ofNullable(cacheMap.get(key));
    }

    @Override
    public List<K> findKeys() {
        return new ArrayList<>(cacheMap.keySet());
    }

    @Override
    public List<V> findValues() {
        return new ArrayList<>(cacheMap.values());
    }

    @Override
    public Optional<K> findByValue(Object value) {
        return cacheMap.keySet().stream().filter(mapValue -> mapValue.equals(value)).findFirst();
    }

    @Override
    public boolean isCached(K key) {
        return cacheMap.containsKey(key);
    }

    @Override
    public boolean isCachedValue(V value) {
        return cacheMap.containsValue(value);
    }

    @Override
    public int getSize() {
        return cacheMap.size();
    }

    @Override
    public void expireAfter(long amount, TimeUnit timeUnit) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable expiration = cacheMap::clear;
        executorService.schedule(expiration, amount, timeUnit);
        executorService.shutdown();
    }

}
