package io.liquorice.config.api.formatter;

import io.liquorice.config.api.storage.ConfigSpace;

import java.util.Optional;

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
     * @return An optional containing the original value, interpreted as type {@link T} if the conversion was
     *         successful, or an {@link Optional#empty()} otherwise
     */
    <T> Optional<T> read(final Object value, Class<T> valueType);

    /**
     * Reformat a value
     *
     * @param value
     *            the object to convert
     * @return A formatted view of $value
     */
    Object write(final Object value);
}
