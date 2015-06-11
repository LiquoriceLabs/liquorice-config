package io.liquorice.core.cache.file;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

import io.liquorice.core.cache.AbstractFlatCacheLayer;
import io.liquorice.core.cache.StorageCacheLayer;
import io.liquorice.core.cache.exception.CacheClearingException;
import io.liquorice.core.cache.exception.CacheInitializationException;
import io.liquorice.core.cache.exception.CacheWarmingException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class PropertiesFileCache extends AbstractFlatCacheLayer implements StorageCacheLayer {
    private Path cacheStorage;

    @Override
    public void clear() throws CacheClearingException {

    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException {
        return getWriteThroughCache().getBoolean(key, defaultValue);
    }

    @Override
    public double getDouble(String key, double defaultValue) throws CacheInitializationException {
        return getWriteThroughCache().getDouble(key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) throws CacheInitializationException {
        return getWriteThroughCache().getInt(key, defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) throws CacheInitializationException {
        return getWriteThroughCache().getString(key, defaultValue);
    }

    @Override
    public Object invalidate(String key) throws CacheInitializationException {
        return null;
    }

    @Override
    public void flush() throws CacheInitializationException {

    }

    @Override
    public Object put(String key, Object value) throws CacheInitializationException {
        return null;
    }

    @Override
    public void putAll(Map<String, Object> elementMap) throws CacheInitializationException {

    }

    @Override
    public Object remove(String key) throws CacheInitializationException {
        return getWriteThroughCache().remove(key);
    }

    @Override
    public void warm(Path path) throws CacheWarmingException {
        this.cacheStorage = path;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }

            @Override
            public void remove() {

            }
        };
    }
}
