package io.liquorice.config.cache.exception;

/**
 * Thrown to indicate a {@link io.liquorice.config.cache.CacheLayer} could not be initialized.
 */
public class CacheInitializationException extends IllegalArgumentException {

    /**
     * Constructs an <code>CacheInitializationException</code> with the specified detail message.
     *
     * @param message
     *            the detail message.
     */
    public CacheInitializationException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param message
     *            the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     * @param cause
     *            the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A
     *            <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public CacheInitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
