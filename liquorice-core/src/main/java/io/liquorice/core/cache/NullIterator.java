package io.liquorice.core.cache;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by mthorpe on 6/16/15.
 */
public class NullIterator implements Iterator<String> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public String next() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {

    }
}
