package io.liquorice.config.storage.inmemory;

import static io.liquorice.config.utils.StringUtils.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.WritableConfigSpace;

/**
 * A {@link Map} backed implementation of a {@link WritableConfigSpace} where the contained items can be modified
 */
public class WritableMapConfigSpace extends ReadableMapConfigSpace implements WritableConfigSpace {

    /**
     * {@inheritDoc}
     */
    public void remove(final String key) {
        requireNonEmpty(key);

        getBackingMap().remove(key);
    }

    /**
     * CTOR
     *
     * @param configFormatter
     *            the {@link ConfigFormatter} to use for writing properties
     * @param properties
     *            the properties to seed this {@link WritableMapConfigSpace} with
     */
    public WritableMapConfigSpace(final ConfigFormatter configFormatter, final Map<String, Object> properties) {
        super(requireNonNull(configFormatter), requireNonNull(properties));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolean(final String key, final boolean value) {
        setObject(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDouble(final String key, final double value) {
        setObject(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(final String key, final int value) {
        setObject(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLong(final String key, final long value) {
        setObject(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(final String key, final Object value) {
        requireNonEmpty(key);
        requireNonNull(value, "Null value. Call ConfigSpace#remove to instead.");

        getBackingMap().put(key, getConfigFormatter().write(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setString(final String key, final String value) {
        setObject(key, value);
    }
}
