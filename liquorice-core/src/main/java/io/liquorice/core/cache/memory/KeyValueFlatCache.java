package io.liquorice.core.cache.memory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.liquorice.core.cache.AbstractFlatCacheLayer;
import io.liquorice.core.cache.FlatCacheLayer;
import io.liquorice.core.cache.exception.CacheClearingException;
import io.liquorice.core.cache.exception.CacheInitializationException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class KeyValueFlatCache extends AbstractFlatCacheLayer implements FlatCacheLayer {
    private Map<String, Object> storage;

    public KeyValueFlatCache() {
        storage = new HashMap<String, Object>();
    }

    @Override
    public void clear() throws CacheClearingException {
        storage.clear();
    }

    @Override
    public void flush() throws CacheInitializationException {
        getWriteThroughCache().putAll(storage);
    }

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

    @Override
    public Object invalidate(String key) throws CacheInitializationException {
        return storage.remove(key);
    }

    @Override
    public Object put(String key, Object value) throws CacheInitializationException {
        return storage.put(key, value);
    }

    @Override
    public void putAll(Map<String, Object> elementMap) throws CacheInitializationException {
        storage.putAll(elementMap);
    }

    @Override
    public Object remove(String key) throws CacheInitializationException {
        getWriteThroughCache().remove(key);
        return invalidate(key);
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            Iterator it = storage.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Object next() {
                return it.next();
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }
}
