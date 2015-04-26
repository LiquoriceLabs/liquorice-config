package io.liquorice.config.api.storage;

import io.liquorice.config.exception.ConfigurationException;

/**
 * An entity storing configuration information
 */
public interface ConfigSpace {

    /**
     * Get the boolean value associated with $key
     *
     * @param key
     *            the key
     * @param defaultValue
     *            a default value if $key could not be found
     * @return the value associated with $key or $defaultValue if it could not be found
     */
    boolean getBoolean(final String key, final boolean defaultValue);

    /**
     * Get the boolean value associated with $key
     *
     * @param key
     *            the key
     * @return the value associated with $key
     * @throws ConfigurationException
     *             if a value associated with $key could not be found
     */
    boolean getBooleanRequired(final String key) throws ConfigurationException;

    /**
     * Get the double value associated with $key
     *
     * @param key
     *            the key
     * @param defaultValue
     *            a default value if $key could not be found
     * @return the value associated with $key or $defaultValue if it could not be found
     */
    double getDouble(final String key, final double defaultValue);

    /**
     * Get the double value associated with $key
     *
     * @param key
     *            the key
     * @return the value associated with $key
     * @throws ConfigurationException
     *             if a value associated with $key could not be found
     */
    double getDoubleRequired(final String key) throws ConfigurationException;

    /**
     * Get the int value associated with $key
     *
     * @param key
     *            the key
     * @param defaultValue
     *            a default value if $key could not be found
     * @return the value associated with $key or $defaultValue if it could not be found
     */
    int getInt(final String key, final int defaultValue);

    /**
     * Get the int value associated with $key
     *
     * @param key
     *            the key
     * @return the value associated with $key
     * @throws ConfigurationException
     *             if a value associated with $key could not be found
     */
    int getIntRequired(final String key) throws ConfigurationException;

    /**
     * Get the long value associated with $key
     *
     * @param key
     *            the key
     * @param defaultValue
     *            a default value if $key could not be found
     * @return the value associated with $key or $defaultValue if it could not be found
     */
    long getLong(final String key, final long defaultValue);

    /**
     * Get the long value associated with $key
     *
     * @param key
     *            the key
     * @return the value associated with $key
     * @throws ConfigurationException
     *             if a value associated with $key could not be found
     */
    long getLongRequired(final String key) throws ConfigurationException;

    /**
     * Get the value associated with $key
     *
     * @param key
     *            the key
     * @param defaultValue
     *            a default value if $key could not be found
     * @param clazz
     *            the class to attempt to cast the object to
     * @param <T>
     *            the type param
     * @return the value associated with $key or $defaultValue if it could not be found
     */
    <T> T getObject(final String key, final T defaultValue, final Class<T> clazz);

    /**
     * Get the value associated with $key
     *
     * @param key
     *            the key
     * @param clazz
     *            the class to attempt to cast the object to
     * @param <T>
     *            the type param
     * @return the value associated with $key
     * @throws ConfigurationException
     *             if a value associated with $key could not be found
     */
    <T> T getObjectRequired(final String key, final Class<T> clazz) throws ConfigurationException;

    /**
     * Get the string value associated with $key
     *
     * @param key
     *            the key
     * @param defaultValue
     *            a default value if $key could not be found
     * @return the value associated with $key or $defaultValue if it could not be found
     */
    String getString(final String key, final String defaultValue);

    /**
     * Get the string value associated with $key
     *
     * @param key
     *            the key
     * @return the value associated with $key
     * @throws ConfigurationException
     *             if a value associated with $key could not be found
     */
    String getStringRequired(final String key) throws ConfigurationException;

    /**
     * Determine whether $key exists in this configuration space
     *
     * @param key
     *            the key
     * @return true if $key exists, false otherwise
     */
    boolean hasValue(final String key);
}
