package io.liquorice.core.cache;

import io.liquorice.core.cache.exception.CacheInitializationException;

/**
 * Created by mthorpe on 6/10/15.
 */
public interface FlatCacheLayer extends CacheLayer {
    FlatCacheLayer getWriteThroughCache() throws CacheInitializationException;

    void setWriteThroughCache(FlatCacheLayer writeThroughCache);
}
