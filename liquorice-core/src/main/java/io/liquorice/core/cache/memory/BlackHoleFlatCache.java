package io.liquorice.core.cache.memory;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import io.liquorice.core.cache.AbstractFlatCacheLayer;
import io.liquorice.core.cache.FlatCacheLayer;
import io.liquorice.core.cache.exception.CacheClearingException;
import io.liquorice.core.cache.exception.CacheInitializationException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class BlackHoleFlatCache extends AbstractFlatCacheLayer implements FlatCacheLayer {

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
}
