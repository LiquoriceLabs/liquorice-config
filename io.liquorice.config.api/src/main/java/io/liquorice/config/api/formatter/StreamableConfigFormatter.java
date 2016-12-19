package io.liquorice.config.api.formatter;

import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;

/**
 * A {@link ConfigFormatter} that supports reading streaming data
 */
public interface StreamableConfigFormatter extends ConfigFormatter {

    /**
     * Interprets a value as an entity of type {@link T}
     *
     * @param reader
     *            a {@link Reader} containing the value to format
     * @param valueType
     *            the type to format the value as
     * @param <T>
     *            type param
     * @return An optional containing the original value, interpreted as type {@link T} if the conversion was
     *         successful, or an {@link Optional#empty()} otherwise
     */
    <T> Optional<T> read(final Reader reader, final Class<T> valueType);

    /**
     * Interprets a value as an entity of type {@link T}
     *
     * @param inputStream
     *            a {@link InputStream} containing the value to format
     * @param valueType
     *            the type to format the value as
     * @param <T>
     *            type param
     * @return An optional containing the original value, interpreted as type {@link T} if the conversion was
     *         successful, or an {@link Optional#empty()} otherwise
     */
    <T> Optional<T> read(final InputStream inputStream, final Class<T> valueType);

    /**
     * Interprets a value as an entity of type {@link T}
     *
     * @param string
     *            a string containing the value to format
     * @param valueType
     *            the type to format the value as
     * @param <T>
     *            type param
     * @return An optional containing the original value, interpreted as type {@link T} if the conversion was
     *         successful, or an {@link Optional#empty()} otherwise
     */
    <T> Optional<T> read(final String string, final Class<T> valueType);
}
