package io.liquorice.config.api.storage;

/**
 * An extension of a {@link ConfigSpace} that allows updating stored entities
 */
public interface WritableConfigSpace extends ConfigSpace {

    /**
     * Associate the $value with the $key
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void setBoolean(final String key, final boolean value);

    /**
     * Associate the $value with the $key
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void setDouble(final String key, final double value);

    /**
     * Associate the $value with the $key
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void setInt(final String key, final int value);

    /**
     * Associate the $value with the $key
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void setLong(final String key, final long value);

    /**
     * Associate the $value with the $key
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void setObject(final String key, final Object value);

    /**
     * Associate the $value with the $key
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void setString(final String key, final String value);
}
