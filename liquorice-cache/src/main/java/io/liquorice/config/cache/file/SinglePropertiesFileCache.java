package io.liquorice.config.cache.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import io.liquorice.config.cache.CacheLayer;
import io.liquorice.config.cache.iterator.NullIterator;
import io.liquorice.config.cache.iterator.PropertiesFileIterator;
import io.liquorice.config.core.logging.Log;
import io.liquorice.config.core.logging.LogFactory;

/**
 * A {@link java.util.Properties} implementation of {@link io.liquorice.config.cache.CacheLayer}
 * 
 * <p>
 * Initialized with either a {@link java.nio.file.Path} or an {@link java.io.InputStream} containing properties in the
 * standard format; delimited by spaces and separated by newline characters:
 *
 * <pre>
 * {@code
 * # This is a comment
 * prop1=value1
 * prop2=value2
 * }
 * </pre>
 *
 * <p>
 * <b>Note:</b> Cache is not usable until {@link #warm} is called
 */
public class SinglePropertiesFileCache extends AbstractFileCache implements CacheLayer {
    private static final Log LOG = LogFactory.getLog(SinglePropertiesFileCache.class);

    /**
     * Provides an iterator that fetches properties from the aforementioned file one at a time
     *
     * @return An iterator over the collection of contained properties
     */
    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        BufferedReader cacheReader = getCacheReader();
        try {
            cacheReader.reset();
            return new PropertiesFileIterator(cacheReader);
        } catch (IOException e) {
            LOG.warning(e);
            return new NullIterator();
        }
    }
}
