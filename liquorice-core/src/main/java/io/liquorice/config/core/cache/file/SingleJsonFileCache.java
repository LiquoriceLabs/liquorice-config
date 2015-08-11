package io.liquorice.config.core.cache.file;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.owlike.genson.Genson;

import io.liquorice.config.core.cache.CacheLayer;
import io.liquorice.config.core.cache.NullIterator;
import io.liquorice.config.core.logging.Log;
import io.liquorice.config.core.logging.LogFactory;

/**
 * Created by mthorpe on 6/10/15.
 */
public class SingleJsonFileCache extends AbstractFileCache implements CacheLayer {
    private static final Log LOG = LogFactory.getLog(SingleJsonFileCache.class);

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        try {
            getCacheReader().reset();
            final Map gensonBackedMap = new Genson().deserialize(getCacheReader(), Map.class);

            return new Iterator<Map.Entry<String, Object>>() {
                Iterator gensonIterator = gensonBackedMap.keySet().iterator();

                @Override
                public boolean hasNext() {
                    return gensonIterator.hasNext();
                }

                @Override
                public Map.Entry<String, Object> next() throws NoSuchElementException {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }

                    String gensonKey = gensonIterator.next().toString();
                    return new AbstractMap.SimpleEntry<>(gensonKey, gensonBackedMap.get(gensonKey));
                }

                @Override
                public void remove() {
                    gensonIterator.remove();
                }
            };
        } catch (IOException e) {
            LOG.warning(e);
            return new NullIterator();
        }
    }
}
