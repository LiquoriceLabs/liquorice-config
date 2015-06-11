package io.liquorice.core.cache.exception;

/**
 * Created by mthorpe on 6/10/15.
 */
public class CacheClearingException extends IllegalArgumentException {
    public CacheClearingException(final String message) {
        super(message);
    }

    public CacheClearingException(final String message, final Exception ex) {
        super(message, ex);
    }
}
