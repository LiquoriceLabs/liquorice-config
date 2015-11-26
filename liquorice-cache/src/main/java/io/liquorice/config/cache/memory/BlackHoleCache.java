package io.liquorice.config.cache.memory;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

import io.liquorice.config.cache.AbstractCacheLayer;
import io.liquorice.config.cache.CacheLayer;
import io.liquorice.config.cache.iterator.NullIterator;
import io.liquorice.config.cache.exception.CacheInitializationException;
import io.liquorice.config.cache.exception.CacheWarmingException;

/**
 * A dummy {@link io.liquorice.config.cache.CacheLayer}, perfectly suited as an end-of-the-line write-through.
 *
 * <p>
 * This cache does not store any data and is immutable. Its sole purpose is to be a low-overhead
 * {@link io.liquorice.config.cache.CacheLayer} that goes at the end of your set of caches. This allows all
 * {@link io.liquorice.config.cache.CacheLayer}s to be agnostic as to their position in the hierarchy. E.g.
 *
 * <pre>
 * {@code
 * // Create CacheLayers
 * CacheLayer topLevelCache = new TopLevelCache();
 * CacheLayer midLevelCache = new MidLevelCache();
 *
 * // Hook them together
 * midLevelCache.setWriteThroughCache(new BlackHoleCache());
 * topLevelCache.setWriteThroughCache(midLevelCache);
 * }
 * </pre>
 */
public class BlackHoleCache extends AbstractCacheLayer implements CacheLayer {

    /**
     * This method does nothing because this {@link io.liquorice.config.cache.CacheLayer} does not store elements
     */
    @Override
    public void clear() {
        // Intentionally unimplemented
    }

    /**
     * Return a default boolean value
     *
     * @param key
     *            ignored
     * @param defaultValue
     *            The value to return
     * @return the value specified by $defaultValue
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException {
        return defaultValue;
    }

    /**
     * Return a default double value
     *
     * @param key
     *            ignored
     * @param defaultValue
     *            The value to return
     * @return the value specified by $defaultValue
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public double getDouble(String key, double defaultValue) throws CacheInitializationException {
        return defaultValue;
    }

    /**
     * Return a default int value
     *
     * @param key
     *            ignored
     * @param defaultValue
     *            The value to return
     * @return the value specified by $defaultValue
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public int getInt(String key, int defaultValue) throws CacheInitializationException {
        return defaultValue;
    }

    /**
     * Return a default {@link String} value
     *
     * @param key
     *            ignored
     * @param defaultValue
     *            The value to return
     * @return the value specified by $defaultValue
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public String getString(String key, String defaultValue) throws CacheInitializationException {
        return defaultValue;
    }

    /**
     * This method does nothing but return null because this {@link io.liquorice.config.cache.CacheLayer} does not
     * store elements.
     *
     * @param key
     *            ignored
     * @return null
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public Object invalidate(String key) throws CacheInitializationException {
        return null;
    }

    /**
     * This method returns a {@link io.liquorice.config.cache.iterator.NullIterator}.
     * 
     * @return a new {@link io.liquorice.config.cache.iterator.NullIterator}
     */
    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return new NullIterator();
    }

    /**
     * This method does nothing because this {@link io.liquorice.config.cache.CacheLayer} does not store elements.
     *
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public void flush() throws CacheInitializationException {
        // Intentionally unimplemented
    }

    /**
     * This method does nothing but return null because this {@link io.liquorice.config.cache.CacheLayer} does not
     * store elements.
     *
     * @param key
     *            ignored
     * @param value
     *            ignored
     * @return null
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public Object put(String key, Object value) throws CacheInitializationException {
        return null;
    }

    /**
     * This method does nothing because this {@link io.liquorice.config.cache.CacheLayer} does not store elements
     *
     * @param elementMap
     *            ignored
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public void putAll(Map<String, Object> elementMap) throws CacheInitializationException {
        // Intentionally unimplemented
    }

    /**
     * This method does nothing but return null because this {@link io.liquorice.config.cache.CacheLayer} does not
     * store elements.
     *
     * @param key
     *            ignored
     * @return null
     * @throws CacheInitializationException
     *             never
     */
    @Override
    public Object remove(String key) throws CacheInitializationException {
        return null;
    }

    /**
     * This method does nothing because this {@link io.liquorice.config.cache.CacheLayer} does not store elements
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
     * This method does nothing because this {@link io.liquorice.config.cache.CacheLayer} does not store elements
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
