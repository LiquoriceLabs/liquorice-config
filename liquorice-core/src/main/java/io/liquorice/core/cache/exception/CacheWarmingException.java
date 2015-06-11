package io.liquorice.core.cache.exception;

/**
 * Created by mthorpe on 6/10/15.
 */
public class CacheWarmingException extends IllegalArgumentException {
    public CacheWarmingException(final String message) {
        super(message);
    }

    public CacheWarmingException(final String message, final Exception ex) {
        super(message, ex);
    }
}
