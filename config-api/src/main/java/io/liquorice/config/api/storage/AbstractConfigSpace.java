package io.liquorice.config.api.storage;

import static java.util.Objects.requireNonNull;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.exception.ConfigurationException;

/**
 * Basic helper implementations of many of the required methods
 *
 * All {@link ConfigFormatter}s should extend this abstract, partial implementation
 */
public abstract class AbstractConfigSpace implements ConfigSpace {

    private final ConfigFormatter configFormatter;

    /**
     * CTOR
     *
     * @param configFormatter
     *            the {@link ConfigFormatter} to use for retrieivng properties
     */
    public AbstractConfigSpace(final ConfigFormatter configFormatter) {
        requireNonNull(configFormatter, "Config formatter cannot be null");
        this.configFormatter = configFormatter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(final String key, final boolean defaultValue) {
        requireNonNull(key, "Key cannot be null");
        try {
            return hasValue(key) ? getBooleanRequired(key) : defaultValue;
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract boolean getBooleanRequired(final String key) throws ConfigurationException;

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(final String key, final double defaultValue) {
        requireNonNull(key, "Key cannot be null");
        try {
            return hasValue(key) ? getDoubleRequired(key) : defaultValue;
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract double getDoubleRequired(final String key) throws ConfigurationException;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(final String key, final int defaultValue) {
        requireNonNull(key, "Key cannot be null");
        try {
            return hasValue(key) ? getIntRequired(key) : defaultValue;
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract int getIntRequired(final String key) throws ConfigurationException;

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(final String key, final long defaultValue) {
        requireNonNull(key, "Key cannot be null");
        try {
            return hasValue(key) ? getLongRequired(key) : defaultValue;
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract long getLongRequired(final String key) throws ConfigurationException;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getObject(final String key, final T defaultValue, final Class<T> clazz) {
        requireNonNull(key, "Key cannot be null");
        try {
            return hasValue(key) ? getObjectRequired(key, clazz) : defaultValue;
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract <T> T getObjectRequired(final String key, final Class<T> clazz) throws ConfigurationException;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(final String key, final String defaultValue) {
        requireNonNull(key, "Key cannot be null");
        try {
            return hasValue(key) ? getStringRequired(key) : defaultValue;
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract String getStringRequired(final String key) throws ConfigurationException;

    /**
     * {@inheritDoc}
     */
    public abstract boolean hasValue(final String key);

    /**
     * @return the configured {@link ConfigFormatter}
     */
    protected ConfigFormatter getConfigFormatter() {
        return configFormatter;
    }
}
