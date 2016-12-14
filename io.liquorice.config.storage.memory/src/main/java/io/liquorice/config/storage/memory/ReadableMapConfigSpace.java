package io.liquorice.config.storage.memory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.AbstractConfigSpace;
import io.liquorice.config.api.storage.ConfigSpace;
import io.liquorice.config.exception.ConfigurationException;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@link Map} backed implementation of a {@link ConfigSpace} where the contained items are unmodifiable after
 * creation
 */
public class ReadableMapConfigSpace extends AbstractConfigSpace implements ConfigSpace {

    private final Map<String, Object> map;

    /**
     * CTOR
     *
     * @param configFormatter
     *            the {@link ConfigFormatter} to use for retrieving properties
     * @param properties
     *            the properties to seed this {@link ReadableMapConfigSpace} with
     */
    public ReadableMapConfigSpace(final ConfigFormatter configFormatter, final Map<String, Object> properties) {
        super(configFormatter);
        map = Maps.newHashMap(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanRequired(final String key) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), Boolean.class);
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    private Object getNonNullable(final String key) {
        final Object value = map.get(key);
        checkNotNull(value);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleRequired(final String key) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), Double.class);
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntRequired(final String key) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), Integer.class);
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongRequired(final String key) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), Long.class);
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param key
     */
    @Override
    public <T> T getObjectRequired(final String key, final Class<T> clazz) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), clazz);
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringRequired(final String key) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), String.class);
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasValue(final String key) {
        return map.containsKey(key);
    }

    /**
     * Get a reference to the map storing all of the contained properties
     *
     * NOTE: This is the actual backing store, not a copy. This method should only be used by extending classes
     *
     * @return the backing store
     */
    protected Map<String, Object> getBackingMap() {
        return map;
    }
}
