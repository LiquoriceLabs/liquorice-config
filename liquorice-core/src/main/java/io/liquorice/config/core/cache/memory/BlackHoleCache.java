package io.liquorice.config.core.cache.memory;

import java.nio.file.Path;
import java.util.Map;

import io.liquorice.config.core.cache.AbstractCacheLayer;
import io.liquorice.config.core.cache.CacheLayer;
import io.liquorice.config.core.cache.exception.CacheClearingException;
import io.liquorice.config.core.cache.exception.CacheInitializationException;
import io.liquorice.config.core.cache.exception.CacheWarmingException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class BlackHoleCache extends AbstractCacheLayer implements CacheLayer {

    @Override
    public void clear() throws CacheClearingException {

    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException {
        return defaultValue;
    }

    @Override
    public double getDouble(String key, double defaultValue) throws CacheInitializationException {
        return defaultValue;
    }

    @Override
    public int getInt(String key, int defaultValue) throws CacheInitializationException {
        return defaultValue;
    }

    @Override
    public String getString(String key, String defaultValue) throws CacheInitializationException {
        return defaultValue;
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
        return null;
    }

    @Override
    public void warm(Path path, String encoding) throws CacheWarmingException {

    }
}
