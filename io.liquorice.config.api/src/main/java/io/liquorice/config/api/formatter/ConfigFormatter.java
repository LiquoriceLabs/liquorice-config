package io.liquorice.config.api.formatter;

import io.liquorice.config.api.storage.ConfigSpace;

/**
 * A pluggable interface for reading and writing non-basic entities to a {@link ConfigSpace}
 */
public interface ConfigFormatter {

    /**
     * Interprets a value as an entity of type {@link T}
     *
     * @param value
     *            the value to format
     * @param valueType
     *            the type to format the value as
     * @param <T>
     *            type param
     * @return The original value, interpreted as type {@link T}
     */
    <T> T read(final Object value, Class<T> valueType);

    /**
     * Reformat a value
     *
     * @param value
     *            the object to convert
     * @param <T>
     *            the type of the object to contain
     * @return A formatted view of $value
     */
    <T> Object write(final Object value);
}
