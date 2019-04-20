package io.liquorice.config.storage.file.properties;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.function.Function;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.WritableConfigSpace;
import io.liquorice.config.exception.ConfigurationException;

/**
 * A {@link java.util.Properties} backed implementation of a {@link WritableConfigSpace} where the contained items can
 * be modified
 */
public class WritablePropertiesFileConfigSpace extends ReadablePropertiesFileConfigSpace implements WritableConfigSpace {

    private static final Function<FileChannel, Writer> DEFAULT_FILE_CHANNEL_WRITER_FUNCTION = internalFileChannel -> Channels
            .newWriter(internalFileChannel, Charsets.UTF_8.name());

    private final FileChannel fileChannel;
    private final Function<FileChannel, Writer> fileChannelWriterFunction;

    private WritablePropertiesFileConfigSpace(final Builder builder) throws IOException {
        super(builder.delegateBuilder.build());
        this.fileChannel = builder.fileChannel;
        this.fileChannelWriterFunction = builder.fileChannelWriterFunction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final String key) {
        checkNotNull(Strings.emptyToNull(key));

        getBackingProperties().remove(key);
        updateOnDiskStore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolean(final String key, final boolean value) {
        setObject(key, Boolean.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDouble(final String key, final double value) {
        setObject(key, Double.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(final String key, final int value) {
        setObject(key, Integer.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLong(final String key, final long value) {
        setObject(key, Long.toString(value));
    }

    /**
     * Associate the $value with the $key
     *
     * <p>
     * This method will update both an in-inmemory cache of the property as well as the on-disk copy of the properties
     * file used to construct this {@link WritablePropertiesFileConfigSpace}
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    @Override
    public void setObject(final String key, final Object value) {
        checkNotNull(Strings.emptyToNull(key));
        checkNotNull(value, "Null value. Call ConfigSpace#remove to instead.");

        // Update the cached copy of the property
        getBackingProperties().setProperty(checkNotNull(key), getConfigFormatter().write(value).toString());

        // Update the on-disk copy of the property
        updateOnDiskStore();
    }

    private void updateOnDiskStore() {
        try {
            try (final Writer writer = fileChannelWriterFunction.apply(fileChannel)) {
                getBackingProperties().store(writer, null);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setString(final String key, final String value) {
        setObject(key, value);
    }

    /**
     * Builder
     */
    public static final class Builder {

        private ReadablePropertiesFileConfigSpace.Builder delegateBuilder;
        private FileChannel fileChannel;
        private Function<FileChannel, Writer> fileChannelWriterFunction;

        /**
         * CTOR
         */
        public Builder() {
            this.delegateBuilder = new ReadablePropertiesFileConfigSpace.Builder();
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
         * @return a new {@link WritablePropertiesFileConfigSpace} built to specification
         * @throws ConfigurationException
         *             if there was a problem building
         */
        public WritablePropertiesFileConfigSpace build() throws ConfigurationException {
            checkNotNull(fileChannel);
            checkNotNull(fileChannelWriterFunction);
            try {
                return new WritablePropertiesFileConfigSpace(this);
            } catch (final IOException e) {
                throw new ConfigurationException(String.format("Error building %s",
                        WritablePropertiesFileConfigSpace.class.getSimpleName()), e);
            }
        }
    }
}
