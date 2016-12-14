package io.liquorice.config.storage.memory;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.WritableConfigSpace;

import java.util.Map;

/**
 * A {@link Map} backed implementation of a {@link WritableConfigSpace} where the contained items can be modified
 */
public class WritableMapConfigSpace extends ReadableMapConfigSpace implements WritableConfigSpace {

    /**
     * CTOR
     *
     * @param configFormatter
     *            the {@link ConfigFormatter} to use for writing properties
     * @param properties
     *            the properties to seed this {@link WritableMapConfigSpace} with
     */
    public WritableMapConfigSpace(final ConfigFormatter configFormatter, final Map<String, Object> properties) {
        super(configFormatter, properties);
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
