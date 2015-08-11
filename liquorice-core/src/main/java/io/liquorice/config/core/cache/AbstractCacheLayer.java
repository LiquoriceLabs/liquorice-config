package io.liquorice.config.core.cache;

import io.liquorice.config.core.cache.exception.CacheInitializationException;

import java.util.Iterator;

/**
 * Created by mthorpe on 6/10/15.
 */
public abstract class AbstractCacheLayer implements CacheLayer {
    private CacheLayer writeThroughCache;

    @Override
    public CacheLayer getWriteThroughCache() {
        if (writeThroughCache == null) {
            throw new CacheInitializationException(this.getClass().getName()
                    + " does not support null write through cache backing");
        } else {
            return writeThroughCache;
        }
    }

    @Override
    public void setWriteThroughCache(CacheLayer writeThroughCache) {
        this.writeThroughCache = writeThroughCache;
    }

    @Override
    public Iterator iterator() {
        return new NullIterator();
    }
}
