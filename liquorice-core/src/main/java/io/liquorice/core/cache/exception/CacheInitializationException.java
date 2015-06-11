package io.liquorice.core.cache.exception;

/**
 * Created by mthorpe on 6/10/15.
 */
public class CacheInitializationException extends IllegalArgumentException {
    public CacheInitializationException(final String message) {
        super(message);
    }

    public CacheInitializationException(final String message, final Exception ex) {
        super(message, ex);
    }
}
