package io.liquorice.config.exception;

/**
 * Thrown to indicate a general configuration exception.
 */
public class ConfigurationException extends IllegalArgumentException {
    /**
     * Constructs an <code>ConfigurationException</code> with the specified detail message.
     *
     * @param message
     *            the detail message.
     */
    public ConfigurationException(final String message) {
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
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
