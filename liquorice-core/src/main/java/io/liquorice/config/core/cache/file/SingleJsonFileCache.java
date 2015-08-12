package io.liquorice.config.core.cache.file;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.owlike.genson.Genson;

import io.liquorice.config.core.cache.CacheLayer;
import io.liquorice.config.core.cache.iterator.NullIterator;
import io.liquorice.config.core.cache.iterator.NebulousMapIterator;
import io.liquorice.config.core.logging.Log;
import io.liquorice.config.core.logging.LogFactory;

/**
 * A Json-backed implementation of {@link io.liquorice.config.core.cache.CacheLayer}
 *
 * <p>
 * Initialized with either a {@link java.nio.file.Path} or an {@link java.io.InputStream} containing properties in
 * standard json format.
 *
 * <p>
 * <b>Note:</b> Cache is not usable until {@link #warm} is called
 */
public class SingleJsonFileCache extends AbstractFileCache implements CacheLayer {
    private static final Log LOG = LogFactory.getLog(SingleJsonFileCache.class);

    /**
     * Provides an iterator that fetches properties from the aforementioned file
     *
     * @return An iterator over the collection of contained properties
     */
    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        try {
            getCacheReader().reset();
            final Map gensonBackedMap = new Genson().deserialize(getCacheReader(), Map.class);
            return new NebulousMapIterator(gensonBackedMap);
        } catch (IOException e) {
            LOG.warning(e);
            return new NullIterator();
        }
    }
}
