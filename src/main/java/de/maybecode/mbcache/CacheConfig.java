package de.maybecode.mbcache;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheConfig {

    void set(String path, Object value);

    <V> void setGenericType(String path, V value);

    void set(String path, Object value, long reloadAfter, TimeUnit timeUnit);

    void replace(String path, Object value);

    void replace(String path, Object value, long reloadAfter, TimeUnit timeUnit);

    Optional<Object> getOptional(String path, Object defaultValue);

    Object get(String path, Object defaultValue);

    <V> V getGenericType(String path, Class<V> clazz);

    String getString(String path, String defaultValue);

    Object getInteger(String path, Integer defaultValue);

    Double getDouble(String path, Double defaultValue);

    Float getFloat(String path, Float defaultValue);

    Long getLong(String path, Long defaultValue);

    Byte getByte(String path, Byte defaultValue);

    Short getShort(String path, Short defaultValue);

    List<?> getList(String path, Object... defaultValue);

    List<String> getStringList(String path, String... defaultValue);

    List<Integer> getIntegerList(String path, Integer... defaultValue);

    boolean contains(String path);

    void save();

    void createFileAndDirectories();

}
