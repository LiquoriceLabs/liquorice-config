package io.liquorice.config.core.cache.iterator;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Iterator for traversing through completely undefined {@link java.util.Map}s, where neither the datatype of the key
 * nor value are known in advance.
 */
public class NebulousMapIterator implements Iterator<Map.Entry<String, Object>> {
    private Map gensonBackedMap;
    private Iterator gensonIterator;

    /**
     * Default CTOR
     * 
     * @param map
     *            The nebulous map to iterate through
     */
    public NebulousMapIterator(final Map map) {
        this.gensonBackedMap = map;
        this.gensonIterator = gensonBackedMap.keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return gensonIterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map.Entry<String, Object> next() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        String gensonKey = gensonIterator.next().toString();
        return new AbstractMap.SimpleEntry<>(gensonKey, gensonBackedMap.get(gensonKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        gensonIterator.remove();
    }
}
