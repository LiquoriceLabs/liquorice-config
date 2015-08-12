package io.liquorice.config.core.cache.memory;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import io.liquorice.config.core.cache.AbstractCacheLayer;
import io.liquorice.config.core.cache.CacheLayer;
import io.liquorice.config.core.cache.exception.CacheInitializationException;
import io.liquorice.config.core.cache.exception.CacheWarmingException;

/**
 * A purely in-memory {@link io.liquorice.config.core.cache.CacheLayer}, backed by a standard {@link java.util.Map}.
 */
public class KeyValueCache extends AbstractCacheLayer implements CacheLayer {
    private Map<String, Object> storage;

    /**
     * Default CTOR
     */
    public KeyValueCache() {
        storage = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        storage.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws CacheInitializationException {
        getWriteThroughCache().putAll(storage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException {
        Object localLookupResult = storage.get(key);
        if (localLookupResult != null && localLookupResult instanceof Boolean) {
            return (Boolean) localLookupResult;
        } else {
            boolean writeThroughCacheResult = getWriteThroughCache().getBoolean(key, defaultValue);
            storage.put(key, writeThroughCacheResult);
            return writeThroughCacheResult;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String key, double defaultValue) throws CacheInitializationException {
        Object localLookupResult = storage.get(key);
        if (localLookupResult != null && localLookupResult instanceof Double) {
            return (Double) localLookupResult;
        } else {
            double writeThroughCacheResult = getWriteThroughCache().getDouble(key, defaultValue);
            storage.put(key, writeThroughCacheResult);
            return writeThroughCacheResult;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String key, int defaultValue) throws CacheInitializationException {
        Object localLookupResult = storage.get(key);
        if (localLookupResult != null && localLookupResult instanceof Integer) {
            return (Integer) localLookupResult;
        } else {
            int writeThroughCacheResult = getWriteThroughCache().getInt(key, defaultValue);
            storage.put(key, writeThroughCacheResult);
            return writeThroughCacheResult;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String key, String defaultValue) throws CacheInitializationException {
        Object localLookupResult = storage.get(key);
        if (localLookupResult != null && localLookupResult instanceof String) {
            return (String) localLookupResult;
        } else {
            String writeThroughCacheResult = getWriteThroughCache().getString(key, defaultValue);
            storage.put(key, writeThroughCacheResult);
            return writeThroughCacheResult;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invalidate(String key) throws CacheInitializationException {
        return storage.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return storage.entrySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object put(String key, Object value) throws CacheInitializationException {
        return storage.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<String, Object> elementMap) throws CacheInitializationException {
        storage.putAll(elementMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object remove(String key) throws CacheInitializationException {
        getWriteThroughCache().remove(key);
        return invalidate(key);
    }

    /**
     * As the {@link io.liquorice.config.core.cache.memory.KeyValueCache} is an in-memory only cache, this method is
     * unused
     *
     * @param path
     *            ignored
     * @param encoding
     *            ignored
     * @throws CacheWarmingException
     *             never
     */
    @Override
    public void warm(Path path, String encoding) throws CacheWarmingException {
        // Intentionally unimplemented
    }

    /**
     * As the {@link io.liquorice.config.core.cache.memory.KeyValueCache} is an in-memory only cache, this method is
     * unused
     *
     * @param inputStream
     *            ignored
     * @param encoding
     *            ignored
     * @throws CacheWarmingException
     *             never
     */
    @Override
    public void warm(InputStream inputStream, String encoding) throws CacheWarmingException {
        // Intentionally unimplemented
    }
}
