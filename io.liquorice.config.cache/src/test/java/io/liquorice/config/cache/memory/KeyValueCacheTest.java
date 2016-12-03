package io.liquorice.config.cache.memory;

import io.liquorice.config.cache.CacheProperties;
import io.liquorice.config.exception.cache.CacheInitializationException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by mthorpe on 6/10/15.
 */
public class KeyValueCacheTest {
    private KeyValueCache keyValueCache;
    private KeyValueCache mockCache;

    @BeforeTest
    public void setup() {
        mockCache = mock(KeyValueCache.class);
        keyValueCache = new KeyValueCache();
        keyValueCache.setWriteThroughCache(mockCache);
        initDefaultAnswers();
    }

    @Test
    public void testClear() {
        keyValueCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        keyValueCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        keyValueCache.clear();

        int actualIntValue = keyValueCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualIntValue, CacheProperties.DEFAULT_INT_VALUE,
                "Cleared cache, but it still contains non-default int value(s)");
        String actualStringValue = keyValueCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualStringValue, CacheProperties.DEFAULT_STRING_VALUE,
                "Cleared cache, but it still contains non-default String value(s)");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testFlushOnUninitializedCache() {
        KeyValueCache uninitializedCache = new KeyValueCache();
        uninitializedCache.flush();
    }

    @Test
    public void testGetBoolean() throws Exception {
        // Store boolean, retrieve boolean
        keyValueCache.put(CacheProperties.BOOLEAN_KEY, CacheProperties.CUSTOM_BOOLEAN_VALUE);
        boolean actualValue = keyValueCache.getBoolean(CacheProperties.BOOLEAN_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualValue, CacheProperties.CUSTOM_BOOLEAN_VALUE, "Failed to retrieve saved value from cache");

        // Retrieve boolean without storing
        actualValue = keyValueCache.getBoolean(CacheProperties.NON_EXISTENT_KEY, CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_BOOLEAN_VALUE, "Retrieved value from cache for unstored key");

        // Store string, retrieve boolean
        keyValueCache.put(CacheProperties.BOOLEAN_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        actualValue = keyValueCache.getBoolean(CacheProperties.BOOLEAN_KEY, CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_BOOLEAN_VALUE,
                "Stored a non-boolean item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetBooleanOnUninitializedCache() {
        KeyValueCache uninitializedCache = new KeyValueCache();
        uninitializedCache.getBoolean(CacheProperties.BOOLEAN_KEY, CacheProperties.DEFAULT_BOOLEAN_VALUE);
    }

    @Test
    public void testGetDouble() throws Exception {
        // Store double, retrieve double
        keyValueCache.put(CacheProperties.DOUBLE_KEY, CacheProperties.CUSTOM_DOUBLE_VALUE);
        double actualValue = keyValueCache.getDouble(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualValue, CacheProperties.CUSTOM_DOUBLE_VALUE, "Failed to retrieve saved value from cache");

        // Retrieve double without storing
        actualValue = keyValueCache.getDouble(CacheProperties.NON_EXISTENT_KEY, CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_DOUBLE_VALUE, "Retrieved value from cache for unstored key");

        // Store string, retrieve double
        keyValueCache.put(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_STRING_VALUE);
        actualValue = keyValueCache.getDouble(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_DOUBLE_VALUE,
                "Stored a non-double item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetDoubleOnUninitializedCache() {
        KeyValueCache uninitializedCache = new KeyValueCache();
        uninitializedCache.getDouble(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_DOUBLE_VALUE);
    }

    @Test
    public void testGetInt() throws Exception {
        // Store int, retrieve int
        keyValueCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        int actualValue = keyValueCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualValue, CacheProperties.CUSTOM_INT_VALUE, "Failed to retrieve saved value from cache");

        // Retrieve int without storing
        actualValue = keyValueCache.getInt("other.key", CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_INT_VALUE, "Retrieved value from cache for unstored key");

        // Store string, retrieve int
        keyValueCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        actualValue = keyValueCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_INT_VALUE,
                "Stored a non-int item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetIntOnUninitializedCache() {
        KeyValueCache uninitializedCache = new KeyValueCache();
        uninitializedCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
    }

    @Test
    public void testGetString() throws Exception {
        // Store string, retrieve string
        keyValueCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        String actualValue = keyValueCache.getString(CacheProperties.STRING_KEY, CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualValue, CacheProperties.CUSTOM_STRING_VALUE, "Failed to retrieve saved value from cache");

        // Retrieve string without storing
        actualValue = keyValueCache.getString("other.key", CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_STRING_VALUE, "Retrieved value from cache for unstored key");

        // Store boolean, retrieve string
        keyValueCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_BOOLEAN_VALUE);
        actualValue = keyValueCache.getString(CacheProperties.STRING_KEY, CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualValue, CacheProperties.DEFAULT_STRING_VALUE,
                "Stored a non-string item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetStringOnUninitializedCache() {
        KeyValueCache uninitializedCache = new KeyValueCache();
        uninitializedCache.getString(CacheProperties.STRING_KEY, CacheProperties.DEFAULT_STRING_VALUE);
    }

    @Test
    public void testInvalidate() {
        keyValueCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        keyValueCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);

        // Invalidate an item
        Object invalidatedValue = keyValueCache.invalidate(CacheProperties.INT_KEY);
        assertTrue(invalidatedValue instanceof Integer);
        int invalidatedInt = (Integer) invalidatedValue;
        assertEquals(invalidatedInt, CacheProperties.CUSTOM_INT_VALUE, "Removed item did not return the saved value");

        // Verify item was invalidated
        int actualIntValue = keyValueCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualIntValue, CacheProperties.DEFAULT_INT_VALUE,
                "Invalidated item was not cleared from the cache");

        // Verify other items were not invalidated
        String actualStringValue = keyValueCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualStringValue, CacheProperties.CUSTOM_STRING_VALUE,
                "Non-invalidated item no longer exists in the cache");
    }

    @Test
    public void testPutAllAndFlush() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        map.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);

        // Dump contents into cache
        keyValueCache.clear();
        keyValueCache.putAll(map);

        // Verify values through raw gets
        int actualIntValue = keyValueCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualIntValue, CacheProperties.CUSTOM_INT_VALUE, "Failed to retrieve saved value from cache");
        String actualStringValue = keyValueCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualStringValue, CacheProperties.CUSTOM_STRING_VALUE,
                "Failed to retrieve saved value from cache");

        // Verify values through iterator
        int counter = 0;
        Iterator<Map.Entry<String, Object>> it = keyValueCache.iterator();
        while (it.hasNext()) {
            counter++;
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            it.remove();

            assertNotNull(map.get(key), "Found entry in cache not stored in original map: [" + key + "|" + value + "]");
            assertEquals(value, map.get(key), "Expected to find [" + map.get(key) + "], but actually found [" + value
                    + "]");
        }
        assertEquals(counter, 2, "Expected to iterator through 2 elements, but found " + counter);

        // Verify flush
        keyValueCache.flush();
        verify(mockCache, times(1)).putAll(anyMap());
    }

    @Test
    public void testRemove() {
        keyValueCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        keyValueCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);

        // Remove an item
        Object invalidatedValue = keyValueCache.remove(CacheProperties.INT_KEY);
        assertTrue(invalidatedValue instanceof Integer);
        int invalidatedInt = (Integer) invalidatedValue;
        assertEquals(invalidatedInt, CacheProperties.CUSTOM_INT_VALUE, "Removed item did not return the saved value");

        // Verify item was removed
        int actualIntValue = keyValueCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualIntValue, CacheProperties.DEFAULT_INT_VALUE, "Removed item was not cleared from the cache");

        // Verify other items were not removed
        String actualStringValue = keyValueCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualStringValue, CacheProperties.CUSTOM_STRING_VALUE,
                "Non-removed item no longer exists in the cache");

        // Verify we attempted to remove the item from the write through cache too
        verify(mockCache, times(1)).remove(anyString());
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testRemoveOnUninitializedCache() {
        KeyValueCache uninitializedCache = new KeyValueCache();
        uninitializedCache.remove(CacheProperties.STRING_KEY);
    }

    private void initDefaultAnswers() {
        when(mockCache.getBoolean(anyString(), any(Boolean.class))).thenAnswer(
                invocation -> invocation.getArguments()[1]);

        when(mockCache.getDouble(anyString(), any(Double.class)))
                .thenAnswer(invocation -> invocation.getArguments()[1]);

        when(mockCache.getInt(anyString(), any(Integer.class))).thenAnswer(invocation -> invocation.getArguments()[1]);

        when(mockCache.getString(anyString(), any(String.class)))
                .thenAnswer(invocation -> invocation.getArguments()[1]);
    }
}
