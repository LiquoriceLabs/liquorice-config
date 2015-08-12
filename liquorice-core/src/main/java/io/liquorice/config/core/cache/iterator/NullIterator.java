package io.liquorice.config.core.cache.iterator;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * End of the line iterator. A {@link NullIterator} never contains entries
 */
public class NullIterator implements Iterator<Map.Entry<String, Object>> {
    /**
     * Returns whether the iterator contains more elements (i.e. Whether {@link #next} would return an element).
     *
     * @return Always false, since this iterator never contains elements
     */
    @Override
    public boolean hasNext() {
        return false;
    }

    /**
     * Returns the next element in this iteration.
     *
     * @return Never returns an element, since this iterator never contains elements
     * @throws NoSuchElementException
     *             always, since this iterator never contains elements
     */
    @Override
    public Map.Entry<String, Object> next() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    /**
     * Removes the last element return by this iterator from the underlying collection.
     */
    @Override
    public void remove() {

    }
}
