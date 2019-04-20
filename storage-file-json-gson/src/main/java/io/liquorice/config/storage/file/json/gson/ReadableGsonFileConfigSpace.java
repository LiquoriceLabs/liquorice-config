package io.liquorice.config.storage.file.json.gson;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.function.Function;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.api.storage.AbstractConfigSpace;
import io.liquorice.config.api.storage.ConfigSpace;
import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.formatter.json.gson.GsonConfigFormatter;

/**
 * A Google/Gson-backed backed implementation of a {@link ConfigSpace} where the contained items are unmodifiable after
 * creation
 */
public class ReadableGsonFileConfigSpace extends AbstractConfigSpace {

    private static final Function<FileChannel, Reader> DEFAULT_FILE_CHANNEL_READER_FUNCTION = internalFileChannel -> Channels
            .newReader(checkNotNull(internalFileChannel), Charsets.UTF_8.name());

    private final FileChannel fileChannel;
    private final Function<FileChannel, Reader> fileChannelReaderFunction;
    private final Gson gson;
    private final JsonObject rootObject;

    /**
     * CTOR
     *
     * @param configSpace
     *            A {@link ReadableGsonFileConfigSpace} to shallow copy
     *
     */
    protected ReadableGsonFileConfigSpace(final ReadableGsonFileConfigSpace configSpace) {
        super(configSpace.getConfigFormatter());
        this.fileChannel = configSpace.fileChannel;
        this.fileChannelReaderFunction = configSpace.fileChannelReaderFunction;
        this.gson = configSpace.gson;
        this.rootObject = configSpace.rootObject;
    }

    private ReadableGsonFileConfigSpace(final Builder builder) throws IOException {
        super(builder.configFormatter);
        this.fileChannel = builder.fileChannel;
        this.fileChannelReaderFunction = builder.fileChannelReaderFunction;
        this.gson = builder.gsonBuilder.create();
        this.rootObject = new JsonParser().parse(gson.newJsonReader(fileChannelReaderFunction.apply(fileChannel)))
                .getAsJsonObject();
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
        return rootObject.has(checkNotNull(Strings.emptyToNull(key)));
    }

    /**
     * Get a reference to the {@link Gson} storing all of the contained properties
     *
     * <p>
     * NOTE: This is the actual backing store, not a copy. This method should only be used by extending classes
     *
     * @return the backing store
     */
    protected Gson getBackingGson() {
        return gson;
    }

    /**
     * Get a reference to the {@link JsonObject} storing all of the contained properties
     *
     * <p>
     * NOTE: This is the actual backing store, not a copy. This method should only be used by extending classes
     *
     * @return the backing store
     */
    protected JsonObject getBackingJsonObject() {
        return rootObject;
    }

    private JsonElement getNonNullable(final String key) {
        final JsonElement value = getBackingJsonObject().get(checkNotNull(Strings.emptyToNull(key)));
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
        private GsonBuilder gsonBuilder;
        private Map<Type, Object> typeAdapters;

        /**
         * CTOR
         */
        public Builder() {
            this.configFormatter = new GsonConfigFormatter.Builder().build();
            this.fileChannelReaderFunction = DEFAULT_FILE_CHANNEL_READER_FUNCTION;
            this.typeAdapters = Maps.newHashMap();
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
         * @return a new {@link ReadableGsonFileConfigSpace} built to specification
         * @throws ConfigurationException
         *             if there was a problem building
         */
        public ReadableGsonFileConfigSpace build() throws ConfigurationException {
            checkNotNull(configFormatter);
            checkNotNull(fileChannel);
            checkNotNull(fileChannelReaderFunction);
            checkNotNull(gsonBuilder);

            for (final Map.Entry<Type, Object> entry : typeAdapters.entrySet()) {
                gsonBuilder.registerTypeAdapter(checkNotNull(entry.getKey()), checkNotNull(entry.getValue()));
            }

            try {
                return new ReadableGsonFileConfigSpace(this);
            } catch (final IOException e) {
                throw new ConfigurationException(String.format("Error building %s",
                        ReadableGsonFileConfigSpace.class.getSimpleName()), e);
            }
        }
    }
}
