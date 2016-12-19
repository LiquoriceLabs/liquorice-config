package io.liquorice.config.storage.memory;

import com.google.common.base.Strings;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.WritableConfigSpace;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@link Map} backed implementation of a {@link WritableConfigSpace} where the contained items can be modified
 */
public class WritableMapConfigSpace extends ReadableMapConfigSpace implements WritableConfigSpace {

    /**
     * {@inheritDoc}
     */
    public void remove(final String key) {
        checkNotNull(Strings.emptyToNull(key));

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
        super(checkNotNull(configFormatter), checkNotNull(properties));
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
        checkNotNull(Strings.emptyToNull(key));
        checkNotNull(value, "Null value. Call ConfigSpace#remove to instead.");

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
