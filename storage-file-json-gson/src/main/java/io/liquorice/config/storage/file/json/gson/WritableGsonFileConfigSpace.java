package io.liquorice.config.storage.file.json.gson;

import static io.liquorice.config.utils.StringUtils.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.WritableConfigSpace;
import io.liquorice.config.exception.ConfigurationException;

/**
 * A Google/Gson-backed implementation of a {@link WritableConfigSpace} where the contained items can be modified
 */
public class WritableGsonFileConfigSpace extends ReadableGsonFileConfigSpace implements WritableConfigSpace {

    private static final Function<FileChannel, Writer> DEFAULT_FILE_CHANNEL_WRITER_FUNCTION = internalFileChannel -> Channels
            .newWriter(internalFileChannel, StandardCharsets.UTF_8.name());

    private final FileChannel fileChannel;
    private final Function<FileChannel, Writer> fileChannelWriterFunction;

    private WritableGsonFileConfigSpace(final Builder builder) {
        super(builder.delegateBuilder.build());
        this.fileChannel = builder.fileChannel;
        this.fileChannelWriterFunction = builder.fileChannelWriterFunction;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(final String key) {
        requireNonEmpty(key);

        // Remove the cached copy of the property
        getBackingJsonObject().remove(key);

        // Update the on-disk copy of the properties
        updateOnDiskStore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolean(final String key, final boolean value) {
        setObject(key, new JsonPrimitive(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDouble(final String key, final double value) {
        setObject(key, new JsonPrimitive(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(final String key, final int value) {
        setObject(key, new JsonPrimitive(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLong(final String key, final long value) {
        setObject(key, new JsonPrimitive(value));
    }

    /**
     * Associate the $value with the $key
     *
     * <p>
     * This method will update both an in-inmemory cache of the property as well as the on-disk copy of the properties
     * file used to construct this {@link WritableGsonFileConfigSpace}.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    @Override
    public void setObject(final String key, final Object value) {
        requireNonEmpty(key);
        requireNonNull(value, "Null value. Call ConfigSpace#remove instead.");

        // Update the cached copy of the property
        final JsonElement jsonElement = value instanceof JsonElement ? (JsonElement) value : getBackingGson()
                .toJsonTree(value);

        getBackingJsonObject().add(key, jsonElement);

        // Update the on-disk copy of the property
        updateOnDiskStore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setString(final String key, final String value) {
        setObject(key, new JsonPrimitive(value));
    }

    private void updateOnDiskStore() {
        try {
            try (final Writer writer = fileChannelWriterFunction.apply(fileChannel)) {
                getBackingGson().toJson(getBackingJsonObject(), writer);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Builder
     */
    public static final class Builder {

        private ReadableGsonFileConfigSpace.Builder delegateBuilder;
        private FileChannel fileChannel;
        private Function<FileChannel, Writer> fileChannelWriterFunction;

        /**
         * CTOR
         */
        public Builder() {
            this.delegateBuilder = new ReadableGsonFileConfigSpace.Builder();
            this.fileChannelWriterFunction = DEFAULT_FILE_CHANNEL_WRITER_FUNCTION;
        }

        /**
         * Overwrite the default {@link ConfigFormatter} with a custom one
         *
         * @param configFormatter
         *            the {@link ConfigFormatter}
         * @return this
         */
        public Builder withConfigFormatter(final ConfigFormatter configFormatter) {
            this.delegateBuilder.withConfigFormatter(configFormatter);
            return this;
        }

        /**
         * Overwrite the default {@link ConfigFormatter} with a custom one
         *
         * @param fileChannel
         *            the {@link FileChannel}
         * @return this
         */
        public Builder withFileChannel(final FileChannel fileChannel) {
            this.delegateBuilder.withFileChannel(fileChannel);
            this.fileChannel = fileChannel;
            return this;
        }

        /**
         * Overwrite the default {@link FileChannel} to {@link Reader} {@link Function} with a custom one
         *
         * <p>
         * This is likely only needed by inheriting classes and tests. The default should be sufficient in most cases.
         *
         * @param fileChannelReaderFunction
         *            the {@link Function}
         * @return this
         */
        public Builder withFileChannelReaderFunction(final Function<FileChannel, Reader> fileChannelReaderFunction) {
            this.delegateBuilder.withFileChannelReaderFunction(fileChannelReaderFunction);
            return this;
        }

        /**
         * Overwrite the default {@link GsonBuilder} with a custom one
         *
         * @param gsonBuilder
         *            the {@link GsonBuilder}
         * @return this
         */
        public Builder withGsonBuilder(final GsonBuilder gsonBuilder) {
            this.delegateBuilder.withGsonBuilder(gsonBuilder);
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
            this.delegateBuilder.withTypeAdapter(type, typeAdapter);
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
            this.delegateBuilder.withTypeAdapters(typeAdapters);
            return this;
        }

        /**
         * Overwrite the default {@link FileChannel} to {@link Writer} {@link Function} with a custom one
         *
         * <p>
         * This is likely only needed by inheriting classes and tests. The default should be sufficient in most cases.
         *
         * @param fileChannelWriterFunction
         *            the {@link Function}
         * @return this
         */
        public Builder withFileChannelWriterFunction(final Function<FileChannel, Writer> fileChannelWriterFunction) {
            this.fileChannelWriterFunction = fileChannelWriterFunction;
            return this;
        }

        /**
         * Build
         *
         * @return a new {@link WritableGsonFileConfigSpace} built to specification
         */
        public WritableGsonFileConfigSpace build() {
            requireNonNull(fileChannel);
            requireNonNull(fileChannelWriterFunction);

            return new WritableGsonFileConfigSpace(this);
        }
    }
}
