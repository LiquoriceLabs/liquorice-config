package io.liquorice.config.cache.iterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Iterator for reading through a standard {@link java.util.Properties} file.
 */
public class PropertiesFileIterator implements Iterator<Map.Entry<String, Object>> {
    private String bufferedLine;
    private BufferedReader bufferedReader;

    /**
     * Default CTOR
     *
     * @param bufferedReader
     *            The {@link BufferedReader} to scan for properties
     */
    public PropertiesFileIterator(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        try {
            do {
                bufferedLine = bufferedReader.readLine();
            } while (bufferedLine != null && (bufferedLine.startsWith("#") || !bufferedLine.contains("=")));
            return bufferedLine != null;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map.Entry<String, Object> next() throws NoSuchElementException {
        if (bufferedLine == null && !hasNext()) {
            throw new NoSuchElementException();
        }

        int idx = bufferedLine.indexOf('=');
        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<String, Object>(bufferedLine.substring(0, idx),
                bufferedLine.substring(idx + 1));
        bufferedLine = null;
        return entry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {

    }
}
