package io.liquorice.config.formatter.json.jackson;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.liquorice.config.api.formatter.StreamableConfigFormatter;

/**
 * An implementation of a {@link StreamableConfigFormatter} where the values are interpreted as JSON strings and the
 * implementation is based around FasterXML/jackson
 */
public class JacksonConfigFormatter implements StreamableConfigFormatter {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    private final ObjectMapper objectMapper;

    private JacksonConfigFormatter(final Builder builder) {
        this.objectMapper = builder.objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final Object value, final Class<T> valueType) {
        if (value instanceof InputStream) {
            return read((InputStream) value, valueType);
        } else if (value instanceof Reader) {
            return read((Reader) value, valueType);
        } else if (value instanceof String) {
            return read((String) value, valueType);
        } else if (value instanceof JsonNode) {
            return read((JsonNode) value, valueType);
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> Optional<T> read(final Reader reader, final Class<T> valueType) {
        try {
            return Optional.ofNullable(objectMapper.readValue(reader, valueType));
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> Optional<T> read(final InputStream inputStream, final Class<T> valueType) {
        try {
            return Optional.ofNullable(objectMapper.readValue(inputStream, valueType));
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> Optional<T> read(final String string, final Class<T> valueType) {
        try {
            return Optional.ofNullable(objectMapper.readValue(string, valueType));
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Interprets a value as an entity of type {@link T}
     *
     * @param jsonNode
     *            a {@link JsonNode} containing the value to format
     * @param valueType
     *            the type to format the value as
     * @param <T>
     *            type param
     * @return An optional containing the original value, interpreted as type {@link T} if the conversion was
     *         successful, or an {@link Optional#empty()} otherwise
     */
    public <T> Optional<T> read(final JsonNode jsonNode, final Class<T> valueType) {
        try {
            return Optional.ofNullable(objectMapper.treeToValue(jsonNode, valueType));
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object write(final Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Builder
     */
    public static final class Builder {
        private Set<Module> modulesToRegister;
        private ObjectMapper objectMapper;

        /**
         * CTOR
         */
        public Builder() {
            this.modulesToRegister = new HashSet<>();
            this.objectMapper = DEFAULT_OBJECT_MAPPER;
        }

        /**
         * Overwrite the default {@link ObjectMapper} with a custom one
         * 
         * @param objectMapper
         *            the object mapper
         * @return this
         */
        public Builder withObjectMapper(final ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        /**
         * Register a {@link Module} with the underlying {@link ObjectMapper}
         *
         * @param module
         *            the module to register
         * @return this
         */
        public Builder withRegisteredModule(final Module module) {
            this.modulesToRegister.add(requireNonNull(module));
            return this;
        }

        /**
         * Register multiple {@link Module}s with the underlying {@link ObjectMapper}
         *
         * @param modules
         *            the {@link Module}s to register
         * @return this
         */
        public Builder withRegisteredModules(final Collection<Module> modules) {
            this.modulesToRegister.addAll(requireNonNull(modules));
            return this;
        }

        /**
         * Build
         * 
         * @return a new {@link JacksonConfigFormatter} built to specification
         */
        public JacksonConfigFormatter build() {
            requireNonNull(objectMapper);
            for (final Module module : modulesToRegister) {
                this.objectMapper.registerModule(requireNonNull(module));
            }
            return new JacksonConfigFormatter(this);
        }
    }
}
