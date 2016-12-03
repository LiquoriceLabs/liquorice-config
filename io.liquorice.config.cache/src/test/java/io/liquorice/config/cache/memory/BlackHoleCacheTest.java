package io.liquorice.config.cache.memory;

import io.liquorice.config.cache.CacheProperties;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

/**
 * Created by mthorpe on 6/14/15.
 */
public class BlackHoleCacheTest {
    private BlackHoleCache cache;

    @BeforeTest
    public void setup() {
        cache = new BlackHoleCache();
        Object existingValue = cache.put(CacheProperties.BOOLEAN_KEY, CacheProperties.CUSTOM_BOOLEAN_VALUE);
        assertNull(existingValue);
        existingValue = cache.put(CacheProperties.DOUBLE_KEY, CacheProperties.CUSTOM_DOUBLE_VALUE);
        assertNull(existingValue);
        existingValue = cache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        assertNull(existingValue);
        existingValue = cache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        assertNull(existingValue);
    }

    @Test
    public void testGetters() {
        boolean actualBoolResult = cache.getBoolean(CacheProperties.BOOLEAN_KEY, CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualBoolResult, CacheProperties.DEFAULT_BOOLEAN_VALUE, "Cache actually returned something:");

        double actualDoubleResult = cache.getDouble(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualDoubleResult, CacheProperties.DEFAULT_DOUBLE_VALUE, "Cache actually returned something:");

        int actualIntResult = cache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualIntResult, CacheProperties.DEFAULT_INT_VALUE, "Cache actually returned something:");

        String actualStringResult = cache.getString(CacheProperties.STRING_KEY, CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualStringResult, CacheProperties.DEFAULT_STRING_VALUE, "Cache actually returned something:");
    }

    @Test
    public void testNullActions() {
        Object actualResult = cache.invalidate(CacheProperties.BOOLEAN_KEY);
        assertNull(actualResult, "Cache actually returned something:");

        actualResult = cache.remove(CacheProperties.BOOLEAN_KEY);
        assertNull(actualResult, "Cache actually returned something:");
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testIteratorActions() {
        Iterator it = cache.iterator();
        assertFalse(it.hasNext());
        it.next();
    }
}
