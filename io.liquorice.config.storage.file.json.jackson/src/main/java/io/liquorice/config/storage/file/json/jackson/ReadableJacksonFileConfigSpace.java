package io.liquorice.config.storage.file.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.AbstractConfigSpace;
import io.liquorice.config.api.storage.ConfigSpace;
import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.formatter.json.jackson.JacksonConfigFormatter;

import java.io.IOException;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A FasterXML/jackson-backed backed implementation of a {@link ConfigSpace} where the contained items are unmodifiable
 * after creation
 */
public class ReadableJacksonFileConfigSpace extends AbstractConfigSpace implements ConfigSpace {

    private static final Function<FileChannel, Reader> DEFAULT_FILE_CHANNEL_READER_FUNCTION = internalFileChannel -> Channels
            .newReader(checkNotNull(internalFileChannel), Charsets.UTF_8.name());

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    private final FileChannel fileChannel;
    private final Function<FileChannel, Reader> fileChannelReaderFunction;
    private final ObjectMapper objectMapper;
    private final JsonNode rootNode;

    /**
     * CTOR
     *
     * @param configSpace
     *            A {@link ReadableJacksonFileConfigSpace} to shallow copy
     *
     */
    protected ReadableJacksonFileConfigSpace(final ReadableJacksonFileConfigSpace configSpace) {
        super(configSpace.getConfigFormatter());
        this.fileChannel = configSpace.fileChannel;
        this.fileChannelReaderFunction = configSpace.fileChannelReaderFunction;
        this.objectMapper = configSpace.objectMapper;
        this.rootNode = configSpace.rootNode;
    }

    private ReadableJacksonFileConfigSpace(final Builder builder) throws IOException {
        super(builder.configFormatter);
        this.fileChannel = builder.fileChannel;
        this.fileChannelReaderFunction = builder.fileChannelReaderFunction;
        this.objectMapper = builder.objectMapper;
        this.rootNode = objectMapper.readTree(this.fileChannelReaderFunction.apply(this.fileChannel));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBooleanRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Boolean.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Double.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLongRequired(final String key) throws ConfigurationException {
        return getObjectRequired(key, Long.class);
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
        return rootNode.hasNonNull(checkNotNull(Strings.emptyToNull(key)));
    }

    /**
     * Get a reference to the {@link JsonNode} storing all of the contained properties
     *
     * <p>
     * NOTE: This is the actual backing store, not a copy. This method should only be used by extending classes
     *
     * @return the backing store
     */
    protected JsonNode getBackingJsonNode() {
        return rootNode;
    }

    /**
     * Get a reference to the {@link ObjectMapper} used to interpret the properties
     *
     * <p>
     * NOTE: This is the actual backing {@link ObjectMapper}, not a copy. This method should only be used by extending
     * classes
     *
     * @return the backing {@link ObjectMapper}
     */
    protected ObjectMapper getBackingObjectMapper() {
        return objectMapper;
    }

    private JsonNode getNonNullable(final String key) {
        final JsonNode value = getBackingJsonNode().get(checkNotNull(Strings.emptyToNull(key)));
        checkNotNull(value);
        return value;
    }

    /**
     * Builder
     */
    public static final class Builder {

        private ConfigFormatter configFormatter;
        private FileChannel fileChannel;
        private Function<FileChannel, Reader> fileChannelReaderFunction;
        private Set<Module> modulesToRegister;
        private ObjectMapper objectMapper;

        /**
         * CTOR
         */
        public Builder() {
            this.configFormatter = new JacksonConfigFormatter.Builder().build();
            this.fileChannelReaderFunction = DEFAULT_FILE_CHANNEL_READER_FUNCTION;
            this.modulesToRegister = Sets.newHashSet();
            this.objectMapper = DEFAULT_OBJECT_MAPPER;
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
         * Register a module with the underlying {@link ObjectMapper}
         *
         * @param module
         *            the {@link Module} to register
         * @return this
         */
        public Builder withRegisteredModule(final Module module) {
            this.modulesToRegister.add(checkNotNull(module));
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
            this.modulesToRegister.addAll(modules);
            return this;
        }

        /**
         * Build
         *
         * @return a new {@link ReadableJacksonFileConfigSpace} built to specification
         * @throws ConfigurationException
         *             if there was a problem building
         */
        public ReadableJacksonFileConfigSpace build() throws ConfigurationException {
            checkNotNull(configFormatter);
            checkNotNull(fileChannel);
            checkNotNull(fileChannelReaderFunction);
            checkNotNull(objectMapper);

            for (final Module module : modulesToRegister) {
                objectMapper.registerModule(checkNotNull(module));
            }

            try {
                return new ReadableJacksonFileConfigSpace(this);
            } catch (final IOException e) {
                throw new ConfigurationException(String.format("Error building %s",
                        ReadableJacksonFileConfigSpace.class.getSimpleName()), e);
            }
        }
    }
}
