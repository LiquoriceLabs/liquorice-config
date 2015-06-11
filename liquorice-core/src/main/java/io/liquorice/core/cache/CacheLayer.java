package io.liquorice.core.cache;

import java.util.Map;

import io.liquorice.core.cache.exception.CacheClearingException;
import io.liquorice.core.cache.exception.CacheInitializationException;

/**
 * Created by mthorpe on 6/10/15.
 */
public interface CacheLayer extends Iterable {
    void clear() throws CacheInitializationException, CacheClearingException;

    void flush() throws CacheInitializationException;

    boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException;

    double getDouble(String key, double defaultValue) throws CacheInitializationException;

    int getInt(String key, int defaultValue) throws CacheInitializationException;

    String getString(String key, String defaultValue) throws CacheInitializationException;

    Object invalidate(String key) throws CacheInitializationException;

    Object put(String key, Object value) throws CacheInitializationException;

    void putAll(Map<String, Object> elementMap) throws CacheInitializationException;

    Object remove(String key) throws CacheInitializationException;
}
