package io.liquorice.core.cache.file;

import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import io.liquorice.core.cache.AbstractFlatCacheLayer;
import io.liquorice.core.cache.StorageCacheLayer;
import io.liquorice.core.cache.exception.CacheClearingException;
import io.liquorice.core.cache.exception.CacheInitializationException;
import io.liquorice.core.cache.exception.CacheWarmingException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class SinglePropertiesFileCache extends AbstractFlatCacheLayer implements StorageCacheLayer {
    private BufferedReader cacheReader;

    @Override
    public void clear() throws CacheClearingException {

    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) throws CacheInitializationException {
        String property = findPropertyInFile(key);
        if(property != null) {
            return property.equalsIgnoreCase("true");
        }
        else {
            return getWriteThroughCache().getBoolean(key, defaultValue);
        }
    }

    @Override
    public double getDouble(String key, double defaultValue) throws CacheInitializationException {
        try {
            String property = findPropertyInFile(key);
            return Double.parseDouble(property);
        }
        catch(NullPointerException | NumberFormatException e) {
            return getWriteThroughCache().getDouble(key, defaultValue);
        }
    }

    @Override
    public int getInt(String key, int defaultValue) throws CacheInitializationException {
        try {
            String property = findPropertyInFile(key);
            return Integer.parseInt(property);
        }
        catch(NullPointerException | NumberFormatException e) {
            return getWriteThroughCache().getInt(key, defaultValue);
        }
    }

    @Override
    public String getString(String key, String defaultValue) throws CacheInitializationException {
        String property = findPropertyInFile(key);
        if(property == null) {
            return getWriteThroughCache().getString(key, defaultValue);
        }
        else {
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
            inputStream = new FileInputStream(path.toFile().getAbsolutePath());
            warm(inputStream, encoding);
        }
        catch(IOException e1) {
            throw new CacheWarmingException("Failed to warm cache", e1);
        }
    }

    public void warm(InputStream inputStream, String encoding) throws CacheWarmingException {
        try {
            this.cacheReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
            this.cacheReader.mark(1 << 24);
            this.cacheReader.reset();
        }
        catch(IOException e) {
            throw new CacheWarmingException("Failed to warm cache", e);
        }
    }

    @Override
    public Iterator iterator() {
        try {
            cacheReader.reset();

            return new Iterator() {
                String bufferedLine;

                @Override
                public boolean hasNext() {
                    try {
                        bufferedLine = cacheReader.readLine();
                        return bufferedLine != null;
                    }
                    catch(IOException e) {
                        return false;
                    }
                }

                @Override
                public Object next() throws NoSuchElementException {
                    if(bufferedLine == null && !hasNext()) {
                        throw new NoSuchElementException();
                    }

                    String line = bufferedLine;
                    bufferedLine = null;
                    return line;
                }

                @Override
                public void remove() {
                    // Unsupported
                }
            };
        }
        catch(IOException e) {
            return new Iterator() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Object next() throws NoSuchElementException {
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {

                }
            };
        }
    }

    private String findPropertyInFile(String propertyName) throws CacheInitializationException {
        Iterator iterator = this.iterator();
        while(iterator.hasNext()) {
            String line = (String) iterator.next();
            if(line.contains("=") && !line.startsWith("#")) {
                String[] parts = line.split("=");
                if (parts[0].equals(propertyName)) {
                    return parts[1];
                }
            }
        }
        return null;
    }
}
