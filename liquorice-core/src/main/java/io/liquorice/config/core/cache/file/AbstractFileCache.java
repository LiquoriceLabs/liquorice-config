package io.liquorice.config.core.cache.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

import io.liquorice.config.core.cache.AbstractCacheLayer;
import io.liquorice.config.core.cache.CacheLayer;
import io.liquorice.config.core.cache.exception.CacheInitializationException;
import io.liquorice.config.core.cache.exception.CacheWarmingException;

/**
 * Shared implementation of any {@link io.liquorice.config.core.cache.CacheLayer} backed by files on a standard
 * filesystem.
 */
public abstract class AbstractFileCache extends AbstractCacheLayer implements CacheLayer {
    private static final int MARK_SIZE_SHIFTER = 24;

    private BufferedReader cacheReader;
    private InputStream inputStream;
    private String encoding;

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException {
        String property = findPropertyValueInFile(key);
        if (property != null) {
            return property.equalsIgnoreCase("true");
        } else {
            return getWriteThroughCache().getBoolean(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String key, double defaultValue) throws CacheInitializationException {
        try {
            String property = findPropertyValueInFile(key);
            return Double.parseDouble(property);
        } catch (NullPointerException | NumberFormatException e) {
            return getWriteThroughCache().getDouble(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String key, int defaultValue) throws CacheInitializationException {
        try {
            String property = findPropertyValueInFile(key);
            return Integer.parseInt(property);
        } catch (NullPointerException | NumberFormatException e) {
            return getWriteThroughCache().getInt(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String key, String defaultValue) throws CacheInitializationException {
        String property = findPropertyValueInFile(key);
        if (property == null) {
            return getWriteThroughCache().getString(key, defaultValue);
        } else {
            return property;
        }
    }

    /**
     * Flushes all data stored in this cache to its write-through {@link io.liquorice.config.core.cache.CacheLayer} and
     * commits any changes to its file-based storage system
     *
     * @throws CacheInitializationException
     *             if the {@link io.liquorice.config.core.cache.CacheLayer} was not initialized properly
     */
    @Override
    public void flush() throws CacheInitializationException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invalidate(String key) throws CacheInitializationException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object put(String key, Object value) throws CacheInitializationException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<String, Object> elementMap) throws CacheInitializationException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object remove(String key) throws CacheInitializationException {
        return getWriteThroughCache().remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warm(Path path, String encoding) throws CacheWarmingException {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new CacheWarmingException("Failed to warm cache", e);
        }

        warm(inputStream, encoding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warm(InputStream inputStream, String encoding) throws CacheWarmingException {
        try {
            this.inputStream = inputStream;
            this.encoding = encoding;
            initReader();
        } catch (IOException e) {
            throw new CacheWarmingException("Failed to warm cache", e);
        }
    }

    /**
     * Get the {@link java.io.BufferedReader} initialized with the preset encoding
     *
     * @return A {@link java.io.BufferedReader} open to the last read position
     */
    protected BufferedReader getCacheReader() {
        return cacheReader;
    }

    private void initReader() throws IOException {
        this.cacheReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        this.cacheReader.mark(1 << MARK_SIZE_SHIFTER);
        this.cacheReader.reset();
    }

    private String findPropertyValueInFile(String propertyName) throws CacheInitializationException {
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            it.remove();

            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) object;
                if (entry.getKey().toString().equals(propertyName)) {
                    return entry.getValue().toString();
                }
            }
        }
        return null;
    }
}
