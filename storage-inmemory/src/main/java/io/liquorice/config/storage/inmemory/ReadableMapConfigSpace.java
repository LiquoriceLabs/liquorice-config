package io.liquorice.config.storage.inmemory;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.AbstractConfigSpace;
import io.liquorice.config.api.storage.ConfigSpace;
import io.liquorice.config.exception.ConfigurationException;

/**
 * A {@link Map} backed implementation of a {@link ConfigSpace} where the contained items are unmodifiable after
 * creation
 */
public class ReadableMapConfigSpace extends AbstractConfigSpace {

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
        super(checkNotNull(configFormatter));
        map = Maps.newHashMap(checkNotNull(properties));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Boolean.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Double.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Long.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getObjectRequired(final String key, final Class<T> clazz) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), clazz).get();
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasValue(final String key) {
        return map.containsKey(checkNotNull(Strings.emptyToNull(key)));
    }

    /**
     * Get a reference to the map storing all of the contained properties
     *
     * <p>
     * NOTE: This is the actual backing store, not a copy. This method should only be used by extending classes
     *
     * @return the backing store
     */
    protected Map<String, Object> getBackingMap() {
        return map;
    }


    private Object getNonNullable(final String key) {
        final Object value = map.get(checkNotNull(Strings.emptyToNull(key)));
        checkNotNull(value);
        return value;
    }
}
