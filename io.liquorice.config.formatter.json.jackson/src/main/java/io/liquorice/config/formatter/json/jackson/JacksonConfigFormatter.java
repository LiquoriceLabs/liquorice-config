package io.liquorice.config.formatter.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.liquorice.config.api.formatter.StreamableConfigFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of a {@link StreamableConfigFormatter} where the values are interpreted as JSON strings and the
 * implementation is based around FasterXML/jackson
 */
public class JacksonConfigFormatter implements StreamableConfigFormatter {

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
     * {@inheritDoc}
     */
    @Override
    public Object write(final Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builder
     */
    public static final class Builder {
        private ObjectMapper objectMapper;

        /**
         * CTOR
         */
        public Builder() {
            this.objectMapper = new ObjectMapper();
        }

        /**
         * Overwrite the default {@link ObjectMapper} with a custom one
         * 
         * @param objectMapper
         *            the object mapper
         * @return this
         */
        public Builder withObjectMapper(final ObjectMapper objectMapper) {
            this.objectMapper = checkNotNull(objectMapper);
            return this;
        }

        /**
         * Register a module with the underlying {@link ObjectMapper}
         *
         * @param module
         *            the module to register
         * @return this
         */
        public Builder withRegisteredModule(final Module module) {
            this.objectMapper.registerModule(checkNotNull(module));
            return this;
        }

        /**
         * Build
         * 
         * @return a new {@link JacksonConfigFormatter} built to specification
         */
        public JacksonConfigFormatter build() {
            return new JacksonConfigFormatter(this);
        }
    }
}
