package io.liquorice.core.cache;

import java.nio.file.Path;

import io.liquorice.core.cache.exception.CacheWarmingException;

/**
 * Created by mthorpe on 6/10/15.
 */
public interface StorageCacheLayer extends FlatCacheLayer {
    void warm(Path path) throws CacheWarmingException;
}
