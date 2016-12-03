package io.liquorice.config.cache;

import io.liquorice.config.exception.cache.CacheInitializationException;

/**
 * Shared implementation of any {@link io.liquorice.config.cache.CacheLayer}
 *
 * <p>
 * Although these methods can be overridden, they provide a basic contract for multi-tiered caching systems
 */
public abstract class AbstractCacheLayer implements CacheLayer {
    private CacheLayer writeThroughCache;

    /**
     * Gets the write-through {@link io.liquorice.config.cache.CacheLayer}.
     *
     * <p>
     * Write-through caches are explained in more detail in the
     * {@link io.liquorice.config.cache.CacheLayer#getWriteThroughCache} method.
     *
     * @return The {@link CacheLayer} one level down from this one
     * @throws CacheInitializationException
     *             if the {@link io.liquorice.config.cache.CacheLayer} was not initialized properly
     */
    @Override
    public CacheLayer getWriteThroughCache() {
        if (writeThroughCache == null) {
            throw new CacheInitializationException(this.getClass().getName()
                    + " does not support null write through cache backing");
        } else {
            return writeThroughCache;
        }
    }

    /**
     * Sets the write-through {@link io.liquorice.config.cache.CacheLayer}.
     *
     * <p>
     * Write-through caches are explained in more detail in the
     * {@link io.liquorice.config.cache.CacheLayer#getWriteThroughCache} method.
     *
     * @param writeThroughCache
     *            The write-through {@link io.liquorice.config.cache.CacheLayer} to forward requests to
     */
    @Override
    public void setWriteThroughCache(CacheLayer writeThroughCache) {
        this.writeThroughCache = writeThroughCache;
    }
}
