package io.liquorice.config.formatter.json.gson;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import io.liquorice.config.api.formatter.StreamableConfigFormatter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of a {@link StreamableConfigFormatter} where the values are interpreted as JSON strings and the
 * implementation is based around Google/Gson
 */
public class GsonConfigFormatter implements StreamableConfigFormatter {

    private static final GsonBuilder DEFAULT_GSON_BUILDER = new GsonBuilder();

    private Gson gson;

    /**
     * CTOR
     */
    private GsonConfigFormatter(final Builder builder) {
        this.gson = builder.gsonBuilder.create();
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
        } else if (value instanceof JsonElement) {
            return read((JsonElement) value, valueType);
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final Reader reader, final Class<T> valueType) {
        try {
            return Optional.ofNullable(gson.fromJson(reader, valueType));
        } catch (final JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final InputStream inputStream, final Class<T> valueType) {
        return read(new InputStreamReader(inputStream, Charsets.UTF_8), valueType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> read(final String string, final Class<T> valueType) {
        try {
            return Optional.ofNullable(gson.fromJson(string, valueType));
        } catch (final JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    /**
     * Interprets a value as an entity of type {@link T}
     *
     * @param jsonElement
     *            a {@link JsonElement} containing the value to format
     * @param valueType
     *            the type to format the value as
     * @param <T>
     *            type param
     * @return An optional containing the original value, interpreted as type {@link T} if the conversion was
     *         successful, or an {@link Optional#empty()} otherwise
     */
    public <T> Optional<T> read(final JsonElement jsonElement, final Class<T> valueType) {
        try {
            return Optional.ofNullable(gson.fromJson(jsonElement, valueType));
        } catch (final JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object write(final Object value) {
        return gson.toJson(value);
    }

    /**
     * Builder
     */
    public static final class Builder {

        private GsonBuilder gsonBuilder;
        private Map<Type, Object> typeAdapters;

        /**
         * CTOR
         */
        public Builder() {
            this.gsonBuilder = DEFAULT_GSON_BUILDER;
            this.typeAdapters = Maps.newHashMap();
        }

        /**
         * Overwrite the default {@link GsonBuilder} with a custom one
         *
         * @param gsonBuilder
         *            the {@link GsonBuilder}
         * @return this
         */
        public Builder withGsonBuilder(final GsonBuilder gsonBuilder) {
            this.gsonBuilder = gsonBuilder;
            return this;
        }

        /**
         * Register a {@link com.google.gson.TypeAdapter} with the underlying {@link GsonBuilder}
         * 
         * @param type
         *            the {@link Type}
         * @param typeAdapter
         *            the type adapter
         * @return this
         */
        public Builder withTypeAdapter(final Type type, final Object typeAdapter) {
            this.typeAdapters.put(type, typeAdapter);
            return this;
        }

        /**
         * Register multiple {@link com.google.gson.TypeAdapter}s with the underlying {@link GsonBuilder}
         *
         * @param typeAdapters
         *            the type adapters
         * @return this
         */
        public Builder withTypeAdapters(final Map<Type, Object> typeAdapters) {
            this.typeAdapters.putAll(typeAdapters);
            return this;
        }

        /**
         * Build
         * 
         * @return a new {@link GsonConfigFormatter} built to specification
         */
        public GsonConfigFormatter build() {
            checkNotNull(gsonBuilder);
            for (final Map.Entry<Type, Object> entry : typeAdapters.entrySet()) {
                this.gsonBuilder.registerTypeAdapter(checkNotNull(entry.getKey()), checkNotNull(entry.getValue()));
            }

            return new GsonConfigFormatter(this);
        }
    }
}
