package io.liquorice.config.storage.file.properties;

import static io.liquorice.config.utils.StringUtils.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Function;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.AbstractConfigSpace;
import io.liquorice.config.api.storage.ConfigSpace;
import io.liquorice.config.exception.ConfigurationException;

/**
 * A FasterXML/jackson-backed implementation of a {@link ConfigSpace} where the contained items are unmodifiable after
 * creation
 */
public class ReadablePropertiesFileConfigSpace extends AbstractConfigSpace {

    private static final Function<FileChannel, Reader> DEFAULT_FILE_CHANNEL_READER_FUNCTION = internalFileChannel -> Channels
            .newReader(requireNonNull(internalFileChannel), StandardCharsets.UTF_8.name());

    private final FileChannel fileChannel;
    private final Function<FileChannel, Reader> fileChannelReaderFunction;
    private final Properties properties;

    /**
     * CTOR
     *
     * @param configSpace
     *            A {@link ReadablePropertiesFileConfigSpace} to shallow copy
     *
     */
    protected ReadablePropertiesFileConfigSpace(final ReadablePropertiesFileConfigSpace configSpace) {
        super(configSpace.getConfigFormatter());
        this.properties = configSpace.properties;
        this.fileChannel = configSpace.fileChannel;
        this.fileChannelReaderFunction = configSpace.fileChannelReaderFunction;
    }

    private ReadablePropertiesFileConfigSpace(final Builder builder) throws IOException {
        super(builder.configFormatter);
        this.properties = new Properties();
        this.fileChannel = builder.fileChannel;
        this.fileChannelReaderFunction = builder.fileChannelReaderFunction;
        properties.load(this.fileChannelReaderFunction.apply(this.fileChannel));
    }

    /**
     * Get the boolean value associated with $key
     *
     * @param key
     *            the key
     * @return the value associated with $key, or false if a problem occurred
     * @throws ConfigurationException
     *             never. Will return false if the key does not exist or the value could not be parsed as a
     *             {@link Double}
     */
    @Override
    public boolean getBooleanRequired(final String key) throws ConfigurationException {
        return Boolean.parseBoolean(getStringRequired(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleRequired(final String key) throws ConfigurationException {
        try {
            return Double.parseDouble(getStringRequired(key));
        } catch (final NumberFormatException e) {
            throw new ConfigurationException("Failed to read property '%s'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntRequired(final String key) throws ConfigurationException {
        try {
            return Integer.parseInt(getStringRequired(key));
        } catch (final NumberFormatException e) {
            throw new ConfigurationException("Failed to read property '%s'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongRequired(final String key) throws ConfigurationException {
        try {
            return Long.parseLong(getStringRequired(key));
        } catch (final NumberFormatException e) {
            throw new ConfigurationException("Failed to read property '%s'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getObjectRequired(final String key, final Class<T> clazz) throws ConfigurationException {
        try {
            return getConfigFormatter().read(getNonNullable(key), clazz).get();
        } catch (final Exception e) {
            throw new ConfigurationException(String.format("Failed to read property '%s'", key), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasValue(final String key) {
        return properties.containsKey(requireNonEmpty(key));
    }

    /**
     * Get a reference to the {@link Properties} storing all of the contained properties
     *
     * <p>
     * NOTE: This is the actual backing store, not a copy. This method should only be used by extending classes
     *
     * @return the backing store
     */
    protected Properties getBackingProperties() {
        return properties;
    }

    private Object getNonNullable(final String key) {
        final Object value = properties.getProperty(requireNonEmpty(key));
        requireNonNull(value);
        return value;
    }

    /**
     * Builder
     */
    public static final class Builder {

        private ConfigFormatter configFormatter;
        private FileChannel fileChannel;
        private Function<FileChannel, Reader> fileChannelReaderFunction;

        /**
         * CTOR
         */
        public Builder() {
            this.fileChannelReaderFunction = DEFAULT_FILE_CHANNEL_READER_FUNCTION;
        }

        /**
         * Overwrite the default {@link ConfigFormatter} with a custom one
         * 
         * @param configFormatter
         *            the {@link ConfigFormatter}
         * @return this
         */
        public Builder withConfigFormatter(final ConfigFormatter configFormatter) {
            this.configFormatter = configFormatter;
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
        protected Builder withFileChannelReaderFunction(final Function<FileChannel, Reader> fileChannelReaderFunction) {
            this.fileChannelReaderFunction = fileChannelReaderFunction;
            return this;
        }

        /**
         * Build
         *
         * @return a new {@link ReadablePropertiesFileConfigSpace} built to specification
         * @throws ConfigurationException
         *             if there was a problem building
         */
        public ReadablePropertiesFileConfigSpace build() throws ConfigurationException {
            requireNonNull(configFormatter);
            requireNonNull(fileChannel);
            requireNonNull(fileChannelReaderFunction);
            try {
                return new ReadablePropertiesFileConfigSpace(this);
            } catch (final IOException e) {
                throw new ConfigurationException(String.format("Error building %s",
                        ReadablePropertiesFileConfigSpace.class.getSimpleName()), e);
            }
        }
    }
}
