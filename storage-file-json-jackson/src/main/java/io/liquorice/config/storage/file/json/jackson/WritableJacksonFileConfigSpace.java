package io.liquorice.config.storage.file.json.jackson;

import static io.liquorice.config.utils.StringUtils.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.WritableConfigSpace;

/**
 * A FasterXML/jackson-backed implementation of a {@link WritableConfigSpace} where the contained items can be modified
 */
public class WritableJacksonFileConfigSpace extends ReadableJacksonFileConfigSpace implements WritableConfigSpace {

    private static final Function<FileChannel, Writer> DEFAULT_FILE_CHANNEL_WRITER_FUNCTION = internalFileChannel -> Channels
            .newWriter(internalFileChannel, StandardCharsets.UTF_8.name());

    private final FileChannel fileChannel;
    private final Function<FileChannel, Writer> fileChannelWriterFunction;

    private WritableJacksonFileConfigSpace(final Builder builder) {
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
        ((ObjectNode) getBackingJsonNode()).remove(key);

        // Update the on-disk copy of the properties
        updateOnDiskStore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolean(final String key, final boolean value) {
        setObject(key, BooleanNode.valueOf(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDouble(final String key, final double value) {
        setObject(key, DoubleNode.valueOf(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(final String key, final int value) {
        setObject(key, IntNode.valueOf(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLong(final String key, final long value) {
        setObject(key, LongNode.valueOf(value));
    }

    /**
     * Associate the $value with the $key
     *
     * <p>
     * This method will update both an in-inmemory cache of the property as well as the on-disk copy of the properties
     * file used to construct this {@link WritableJacksonFileConfigSpace}.
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
        final JsonNode jsonNode = value instanceof JsonNode ? (JsonNode) value : //
                getBackingObjectMapper().valueToTree(value);
        ((ObjectNode) getBackingJsonNode()).replace(key, jsonNode);

        // Update the on-disk copy of the property
        updateOnDiskStore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setString(final String key, final String value) {
        setObject(key, TextNode.valueOf(value));
    }

    private void updateOnDiskStore() {
        try {
            try (final Writer writer = fileChannelWriterFunction.apply(fileChannel)) {
                getBackingObjectMapper().writeValue(writer, getBackingJsonNode());
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Builder
     */
    public static final class Builder {

        private ReadableJacksonFileConfigSpace.Builder delegateBuilder;
        private FileChannel fileChannel;
        private Function<FileChannel, Writer> fileChannelWriterFunction;

        /**
         * CTOR
         */
        public Builder() {
            this.delegateBuilder = new ReadableJacksonFileConfigSpace.Builder();
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
         * Overwrite the default {@link ObjectMapper} with a custom one
         *
         * @param objectMapper
         *            the object mapper
         * @return this
         */
        public Builder withObjectMapper(final ObjectMapper objectMapper) {
            this.delegateBuilder.withObjectMapper(objectMapper);
            return this;
        }

        /**
         * Register a module with the underlying {@link ObjectMapper}
         *
         * @param module
         *            the {@link Module} to register
         * @return this
         */
        public Builder withRegisteredModule(final Module module) {
            this.delegateBuilder.withRegisteredModule(module);
            return this;
        }

        /**
         * Register a module with the underlying {@link ObjectMapper}
         *
         * @param modules
         *            the {@link Module}s to register
         * @return this
         */
        public Builder withRegisteredModules(final Collection<Module> modules) {
            this.delegateBuilder.withRegisteredModules(modules);
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
         * @return a new {@link WritableJacksonFileConfigSpace} built to specification
         */
        public WritableJacksonFileConfigSpace build() {
            requireNonNull(fileChannel);
            requireNonNull(fileChannelWriterFunction);

            return new WritableJacksonFileConfigSpace(this);
        }
    }
}
