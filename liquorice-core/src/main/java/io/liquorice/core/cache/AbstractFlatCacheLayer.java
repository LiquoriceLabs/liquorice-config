package io.liquorice.core.cache;

import io.liquorice.core.cache.exception.CacheInitializationException;

import java.util.Iterator;

/**
 * Created by mthorpe on 6/10/15.
 */
public abstract class AbstractFlatCacheLayer implements FlatCacheLayer {
    private FlatCacheLayer writeThroughCache;

    @Override
    public FlatCacheLayer getWriteThroughCache() {
        if (writeThroughCache == null) {
            throw new CacheInitializationException(this.getClass().getName()
                    + " does not support null write through cache backing");
        } else {
            return writeThroughCache;
        }
    }

    @Override
    public void setWriteThroughCache(FlatCacheLayer writeThroughCache) {
        this.writeThroughCache = writeThroughCache;
    }

    @Override
    public Iterator iterator() {
        return new NullIterator();
    }
}
