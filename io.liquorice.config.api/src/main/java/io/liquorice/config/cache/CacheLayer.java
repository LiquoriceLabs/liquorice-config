package io.liquorice.config.cache;

import io.liquorice.config.exception.cache.CacheInitializationException;
import io.liquorice.config.exception.cache.CacheWarmingException;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * The basic building block for all configuration services.
 * 
 * <p>
 * All layers must inherit from this interface to ensure interoperability with other layers. Layers can be arranged in
 * any sequence with read-through and write-through allowing interactions from higher-level to lower-level layers.
 * Write-through caches are explained in more detail in the {@link #getWriteThroughCache} method.
 */
public interface CacheLayer extends Iterable {
    /**
     * Remove all elements currently stored in this {@link CacheLayer}.
     * 
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    void clear() throws CacheInitializationException;

    /**
     * Flushes all data stored in this cache to its write-through {@link CacheLayer}, then clears
     * 
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    void flush() throws CacheInitializationException;

    /**
     * Get a boolean item stored in this cache
     *
     * @param key
     *            The key to search for
     * @param defaultValue
     *            The default value to return if the key was not found or not a boolean
     * @return The value stored in the cache if exists and can be represented as a boolean, else $defaultValue
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException;

    /**
     * Get a double item stored in this cache
     *
     * @param key
     *            The key to search for
     * @param defaultValue
     *            The default value to return if the key was not found or not a double
     * @return The value stored in the cache if exists and can be represented as a double, else $defaultValue
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    double getDouble(String key, double defaultValue) throws CacheInitializationException;

    /**
     * Get an int item stored in this cache
     *
     * @param key
     *            The key to search for
     * @param defaultValue
     *            The default value to return if the key was not found or not a int
     * @return The value stored in the cache if exists and can be represented as a int, else $defaultValue
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    int getInt(String key, int defaultValue) throws CacheInitializationException;

    /**
     * Get a {@link String} item stored in this cache
     *
     * @param key
     *            The key to search for
     * @param defaultValue
     *            The default value to return if the key was not found or not a {@link String}
     * @return The value stored in the cache if exists and can be represented as a {@link String}, else $defaultValue
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    String getString(String key, String defaultValue) throws CacheInitializationException;

    /**
     * The {@link CacheLayer} to forward requests to.
     *
     * <p>
     * Every {@link CacheLayer} must have write-through cache assigned. When in doubt, use the
     * {@link io.liquorice.config.cache.memory.BlackHoleCache}. The write through cache will be used in two places:
     * <ul>
     * <li>If an element is requested that does not exist in this cache, the request is forwarded to the write-through
     * cache then cached in this one upon return</li>
     * <li>If {@link #flush} is called, this {@link CacheLayer} will push its elements to the write-through cache</li>
     * </ul>
     *
     * @return The {@link CacheLayer} one level down from this one
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    CacheLayer getWriteThroughCache() throws CacheInitializationException;

    /**
     * Sets the write-through {@link CacheLayer}.
     *
     * <p>
     * Write-through caches are explained in more detail in the {@link CacheLayer#getWriteThroughCache} method.
     * 
     * @param writeThroughCache
     *            The write-through {@link CacheLayer} to forward requests to
     */
    void setWriteThroughCache(CacheLayer writeThroughCache);

    /**
     * Purge a single element from this {@link CacheLayer}.
     * 
     * @param key
     *            The key of the element to purge
     * @return The element currently stored with the given $key
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    Object invalidate(String key) throws CacheInitializationException;

    /**
     * Store a single element in this {@link CacheLayer}.
     * 
     * @param key
     *            The key of the element to store
     * @param value
     *            The element to store
     * @return The element already stored at the given $key
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    Object put(String key, Object value) throws CacheInitializationException;

    /**
     * Store a group of elements in this {@link CacheLayer}.
     * 
     * @param elementMap
     *            A map containing the elements to store
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    void putAll(Map<String, Object> elementMap) throws CacheInitializationException;

    /**
     * Purge an element from not only this cache, but all lower-level caches too.
     *
     * <p>
     * Like {@link #invalidate}, but more of a nuclear option.
     * 
     * @param key
     *            The key of the element to purge
     * @return The element currently stored with the given $key
     * @throws CacheInitializationException
     *             if the {@link CacheLayer} was not initialized properly
     */
    Object remove(String key) throws CacheInitializationException;

    /**
     * Convenience method for initializing this {@link CacheLayer} with already existing data.
     *
     * @param path
     *            A {@link Path} representing a file or directory to bulk load elements from
     * @param encoding
     *            The character encoding to read any files as
     * @throws CacheWarmingException
     *             if the {@link CacheLayer} could not be warmed
     */
    void warm(Path path, String encoding) throws CacheWarmingException;

    /**
     * Convenience method for initializing this {@link CacheLayer} with already existing data.
     * 
     * @param inputStream
     *            An {@link InputStream} to bulk load elements from
     * @param encoding
     *            The character encoding to read the stream as
     * @throws CacheWarmingException
     *             if the {@link CacheLayer} could not be warmed
     */
    void warm(InputStream inputStream, String encoding) throws CacheWarmingException;
}
