package io.liquorice.config.core.cache.memory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import io.liquorice.config.core.cache.AbstractCacheLayer;
import io.liquorice.config.core.cache.CacheLayer;
import io.liquorice.config.core.cache.exception.CacheClearingException;
import io.liquorice.config.core.cache.exception.CacheInitializationException;
import io.liquorice.config.core.cache.exception.CacheWarmingException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class KeyValueCache extends AbstractCacheLayer implements CacheLayer {
    private Map<String, Object> storage;

    public KeyValueCache() {
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
    public void warm(Path path, String encoding) throws CacheWarmingException {

    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return new Iterator<Map.Entry<String, Object>>() {
            Iterator<Map.Entry<String, Object>> it = storage.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Map.Entry<String, Object> next() throws NoSuchElementException {
                if (it.hasNext()) {
                    return it.next();
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }
}
