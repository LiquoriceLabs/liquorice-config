package io.liquorice.config.core.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by mthorpe on 6/16/15.
 */
public class NullIterator implements Iterator<Map.Entry<String, Object>> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Map.Entry<String, Object> next() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {

    }
}
