package de.maybecode.mbcache.config;

import de.maybecode.mbcache.CacheConfig;
import de.maybecode.mbcache.CacheMap;
import de.maybecode.mbcache.cache.MBCache;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MBCacheConfig implements CacheConfig {

    private final File file;
    private final YamlConfiguration configuration;

    private final CacheMap<String, Object> configCache = new MBCache<>();

    public MBCacheConfig(String file) {
        this.file = new File(file);
        createFileAndDirectories();
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public MBCacheConfig(String file, long expireAfter, TimeUnit timeUnit) {
        this.file = new File(file);
        createFileAndDirectories();
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        this.configCache.expireAfter(expireAfter, timeUnit);
    }

    @Override
    public void set(String path, Object value) {
        this.configCache.cache(path, value);
        if (!this.configuration.contains(path)) {
            this.configuration.set(path, value);
            save();
        }
    }

    @Override
    public void set(String path, Object value, long reloadAfter, TimeUnit timeUnit) {
        this.configCache.cache(path, value, reloadAfter, timeUnit);
        if (!this.configuration.contains(path)) {
            this.configuration.set(path, value);
            save();
        }
    }

    @Override
    public void replace(String path, Object value) {
        this.configCache.reloadCache(path, value);
        this.configuration.set(path, value);
        save();

    }

    @Override
    public void replace(String path, Object value, long reloadAfter, TimeUnit timeUnit) {
        this.configCache.reloadCache(path, value, reloadAfter, timeUnit);
        this.configuration.set(path, value);
        save();
    }

    @Override
    public Optional<Object> getOptional(String path, Object defaultValue) {
        if (this.configCache.isCached(path))
            return this.configCache.findByKey(path);
        set(path, defaultValue);
        return this.configCache.findByKey(path);
    }

    @Override
    public Object get(String path, Object defaultValue) {
        return getOptional(path, defaultValue).orElse(defaultValue);
    }

    @Override
    public String getString(String path, String defaultValue) {
        return String.valueOf(getOptional(path, defaultValue).orElse(defaultValue));
    }

    @Override
    public Integer getInteger(String path, Integer defaultValue) {
        return (Integer) getOptional(path, defaultValue).orElse(defaultValue);
    }

    @Override
    public Double getDouble(String path, Double defaultValue) {
        return (Double) getOptional(path, defaultValue).orElse(defaultValue);
    }

    @Override
    public Float getFloat(String path, Float defaultValue) {
        return (Float) getOptional(path, defaultValue).orElse(defaultValue);
    }

    @Override
    public Long getLong(String path, Long defaultValue) {
        return (Long) getOptional(path, defaultValue).orElse(defaultValue);
    }

    @Override
    public Byte getByte(String path, Byte defaultValue) {
        return (Byte) getOptional(path, defaultValue).orElse(defaultValue);
    }

    @Override
    public Short getShort(String path, Short defaultValue) {
        return (Short) getOptional(path, defaultValue).orElse(defaultValue);
    }

    @Override
    public List<?> getList(String path, Object... defaultValue) {
        List<Object> defaultList = new ArrayList<>(Arrays.asList(defaultValue));
        if (this.configCache.isCached(path)) {
            return (List<?>) this.configCache.findByKey(path).orElse(defaultList);
        }
        set(path, defaultValue);
        return (List<?>) this.configCache.findByKey(path).orElse(defaultList);
    }

    @Override
    public List<String> getStringList(String path, String... defaultValue) {
        List<String> defaultList = new ArrayList<>(Arrays.asList(defaultValue));
        if (this.configCache.isCached(path)) {
            return (List<String>) this.configCache.findByKey(path).orElse(defaultList);
        }
        if (this.configuration.contains(path)) {
            this.configCache.cache(path, defaultList);
            return this.configuration.getStringList(path);
        } else {
            this.configuration.set(path, defaultList);
            save();
            return defaultList;
        }
    }

    @Override
    public List<Integer> getIntegerList(String path, Integer... defaultValue) {
        List<Integer> defaultList = new ArrayList<>(Arrays.asList(defaultValue));
        if (this.configCache.isCached(path)) {
            return (List<Integer>) this.configCache.findByKey(path).orElse(defaultList);
        }
        set(path, defaultValue);
        return this.configuration.getIntList(path);
    }

    @Override
    public boolean contains(String path) {
        return this.configuration.contains(path) || (this.configCache.isCached(path));
    }

    @Override
    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createFileAndDirectories() {
        if (this.file.getParentFile() != null)
            this.file.getParentFile().mkdirs();
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
