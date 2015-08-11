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
import io.liquorice.config.core.cache.exception.CacheClearingException;
import io.liquorice.config.core.cache.exception.CacheInitializationException;
import io.liquorice.config.core.cache.exception.CacheWarmingException;

/**
 * Created by mthorpe on 8/11/15.
 */
public abstract class AbstractFileCache extends AbstractCacheLayer implements CacheLayer {
    private static final int MARK_SIZE_SHIFTER = 24;

    private BufferedReader cacheReader;
    private InputStream inputStream;
    private String encoding;

    @Override
    public void clear() throws CacheClearingException {

    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException {
        String property = findPropertyValueInFile(key);
        if (property != null) {
            return property.equalsIgnoreCase("true");
        } else {
            return getWriteThroughCache().getBoolean(key, defaultValue);
        }
    }

    @Override
    public double getDouble(String key, double defaultValue) throws CacheInitializationException {
        try {
            String property = findPropertyValueInFile(key);
            return Double.parseDouble(property);
        } catch (NullPointerException | NumberFormatException e) {
            return getWriteThroughCache().getDouble(key, defaultValue);
        }
    }

    @Override
    public int getInt(String key, int defaultValue) throws CacheInitializationException {
        try {
            String property = findPropertyValueInFile(key);
            return Integer.parseInt(property);
        } catch (NullPointerException | NumberFormatException e) {
            return getWriteThroughCache().getInt(key, defaultValue);
        }
    }

    @Override
    public String getString(String key, String defaultValue) throws CacheInitializationException {
        String property = findPropertyValueInFile(key);
        if (property == null) {
            return getWriteThroughCache().getString(key, defaultValue);
        } else {
            return property;
        }
    }

    @Override
    public Object invalidate(String key) throws CacheInitializationException {
        return null;
    }

    @Override
    public void flush() throws CacheInitializationException {

    }

    @Override
    public Object put(String key, Object value) throws CacheInitializationException {
        return null;
    }

    @Override
    public void putAll(Map<String, Object> elementMap) throws CacheInitializationException {

    }

    @Override
    public Object remove(String key) throws CacheInitializationException {
        return getWriteThroughCache().remove(key);
    }

    @Override
    public void warm(Path path, String encoding) throws CacheWarmingException {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(path.toFile());
            warm(inputStream, encoding);
        } catch (IOException e1) {
            throw new CacheWarmingException("Failed to warm cache", e1);
        }
    }

    public void warm(InputStream inputStream, String encoding) throws CacheWarmingException {
        try {
            this.inputStream = inputStream;
            this.encoding = encoding;
            initReader();
        } catch (IOException e) {
            throw new CacheWarmingException("Failed to warm cache", e);
        }
    }

    protected BufferedReader getCacheReader() {
        return cacheReader;
    }

    private void initReader() throws IOException {
        this.cacheReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        this.cacheReader.mark(1 << MARK_SIZE_SHIFTER);
        this.cacheReader.reset();
    }

    private String findPropertyValueInFile(String propertyName) throws CacheInitializationException {
        Iterator<Map.Entry<String, Object>> it = this.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            it.remove();

            if (entry.getKey().equals(propertyName)) {
                return entry.getValue().toString();
            }
        }
        return null;
    }
}
