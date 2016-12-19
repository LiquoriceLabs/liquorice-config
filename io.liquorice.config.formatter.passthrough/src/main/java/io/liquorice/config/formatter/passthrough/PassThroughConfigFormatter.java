package io.liquorice.config.formatter.passthrough;

import io.liquorice.config.api.formatter.ConfigFormatter;

import java.util.Optional;

/**
 * A trivial implementation of a {@link ConfigFormatter} that blindly attempts to cast the object as whatever was
 * requested
 */
public class PassThroughConfigFormatter implements ConfigFormatter {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final Object value, final Class<T> valueType) {
        return Optional.ofNullable(valueType.cast(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object write(final Object value) {
        return value;
    }
}
