package io.liquorice.core.cache.memory;

import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.liquorice.core.cache.CacheProperties;
import io.liquorice.core.cache.exception.CacheInitializationException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class KeyValueFlatCacheTest {
    private KeyValueFlatCache keyValueFlatCache;
    private KeyValueFlatCache mockFlatCache;

    @BeforeTest
    public void setup() {
        mockFlatCache = Mockito.mock(KeyValueFlatCache.class);
        keyValueFlatCache = new KeyValueFlatCache();
        keyValueFlatCache.setWriteThroughCache(mockFlatCache);
        initDefaultAnswers();
    }

    @Test
    public void testClear() {
        keyValueFlatCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        keyValueFlatCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        keyValueFlatCache.clear();

        int actualIntValue = keyValueFlatCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualIntValue, CacheProperties.DEFAULT_INT_VALUE,
                "Cleared cache, but it still contains non-default int value(s)");
        String actualStringValue = keyValueFlatCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualStringValue, CacheProperties.DEFAULT_STRING_VALUE,
                "Cleared cache, but it still contains non-default String value(s)");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testFlushOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.flush();
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testGetBoolean() throws Exception {
        // Store boolean, retrieve boolean
        keyValueFlatCache.put(CacheProperties.BOOLEAN_KEY, CacheProperties.CUSTOM_BOOLEAN_VALUE);
        boolean actualValue = keyValueFlatCache.getBoolean(CacheProperties.BOOLEAN_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.CUSTOM_BOOLEAN_VALUE,
                "Failed to retrieve saved value from cache");

        // Retrieve boolean without storing
        actualValue = keyValueFlatCache.getBoolean(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_BOOLEAN_VALUE,
                "Retrieved value from cache for unstored key");

        // Store string, retrieve boolean
        keyValueFlatCache.put(CacheProperties.BOOLEAN_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        actualValue = keyValueFlatCache.getBoolean(CacheProperties.BOOLEAN_KEY, CacheProperties.DEFAULT_BOOLEAN_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_BOOLEAN_VALUE,
                "Stored a non-boolean item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetBooleanOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getBoolean(CacheProperties.BOOLEAN_KEY, CacheProperties.DEFAULT_BOOLEAN_VALUE);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testGetDouble() throws Exception {
        // Store double, retrieve double
        keyValueFlatCache.put(CacheProperties.DOUBLE_KEY, CacheProperties.CUSTOM_DOUBLE_VALUE);
        double actualValue = keyValueFlatCache.getDouble(CacheProperties.DOUBLE_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.CUSTOM_DOUBLE_VALUE,
                "Failed to retrieve saved value from cache");

        // Retrieve double without storing
        actualValue = keyValueFlatCache.getDouble(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_DOUBLE_VALUE,
                "Retrieved value from cache for unstored key");

        // Store string, retrieve double
        keyValueFlatCache.put(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_STRING_VALUE);
        actualValue = keyValueFlatCache.getDouble(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_DOUBLE_VALUE,
                "Stored a non-double item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetDoubleOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getDouble(CacheProperties.DOUBLE_KEY, CacheProperties.DEFAULT_DOUBLE_VALUE);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testGetInt() throws Exception {
        // Store int, retrieve int
        keyValueFlatCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        int actualValue = keyValueFlatCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.CUSTOM_INT_VALUE, "Failed to retrieve saved value from cache");

        // Retrieve int without storing
        actualValue = keyValueFlatCache.getInt("other.key", CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_INT_VALUE,
                "Retrieved value from cache for unstored key");

        // Store string, retrieve int
        keyValueFlatCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        actualValue = keyValueFlatCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_INT_VALUE,
                "Stored a non-int item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetIntOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testGetString() throws Exception {
        // Store string, retrieve string
        keyValueFlatCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        String actualValue = keyValueFlatCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.CUSTOM_STRING_VALUE,
                "Failed to retrieve saved value from cache");

        // Retrieve string without storing
        actualValue = keyValueFlatCache.getString("other.key", CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_STRING_VALUE,
                "Retrieved value from cache for unstored key");

        // Store boolean, retrieve string
        keyValueFlatCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_BOOLEAN_VALUE);
        actualValue = keyValueFlatCache.getString(CacheProperties.STRING_KEY, CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualValue, CacheProperties.DEFAULT_STRING_VALUE,
                "Stored a non-string item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetStringOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getString(CacheProperties.STRING_KEY, CacheProperties.DEFAULT_STRING_VALUE);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testInvalidate() {
        keyValueFlatCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        keyValueFlatCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);

        // Invalidate an item
        Object invalidatedValue = keyValueFlatCache.invalidate(CacheProperties.INT_KEY);
        Assert.assertTrue(invalidatedValue instanceof Integer);
        int invalidatedInt = (Integer) invalidatedValue;
        Assert.assertEquals(invalidatedInt, CacheProperties.CUSTOM_INT_VALUE,
                "Removed item did not return the saved value");

        // Verify item was invalidated
        int actualIntValue = keyValueFlatCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualIntValue, CacheProperties.DEFAULT_INT_VALUE,
                "Invalidated item was not cleared from the cache");

        // Verify other items were not invalidated
        String actualStringValue = keyValueFlatCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualStringValue, CacheProperties.CUSTOM_STRING_VALUE,
                "Non-invalidated item no longer exists in the cache");
    }

    @Test
    public void testPutAllAndFlush() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        map.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);

        // Dump contents into cache
        keyValueFlatCache.putAll(map);

        // Verify values
        int actualIntValue = keyValueFlatCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualIntValue, CacheProperties.CUSTOM_INT_VALUE,
                "Failed to retrieve saved value from cache");
        String actualStringValue = keyValueFlatCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualStringValue, CacheProperties.CUSTOM_STRING_VALUE,
                "Failed to retrieve saved value from cache");

        // Verify flush
        keyValueFlatCache.flush();
        Mockito.verify(mockFlatCache, Mockito.times(1)).putAll(Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void testRemove() {
        keyValueFlatCache.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        keyValueFlatCache.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);

        // Remove an item
        Object invalidatedValue = keyValueFlatCache.remove(CacheProperties.INT_KEY);
        Assert.assertTrue(invalidatedValue instanceof Integer);
        int invalidatedInt = (Integer) invalidatedValue;
        Assert.assertEquals(invalidatedInt, CacheProperties.CUSTOM_INT_VALUE,
                "Removed item did not return the saved value");

        // Verify item was removed
        int actualIntValue = keyValueFlatCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualIntValue, CacheProperties.DEFAULT_INT_VALUE,
                "Removed item was not cleared from the cache");

        // Verify other items were not removed
        String actualStringValue = keyValueFlatCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualStringValue, CacheProperties.CUSTOM_STRING_VALUE,
                "Non-removed item no longer exists in the cache");

        // Verify we attempted to remove the item from the write through cache too
        Mockito.verify(mockFlatCache, Mockito.times(1)).remove(Mockito.anyString());
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testRemoveOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.remove(CacheProperties.STRING_KEY);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    private void initDefaultAnswers() {
        Mockito.when(mockFlatCache.getBoolean(Mockito.anyString(), Mockito.any(Boolean.class))).thenAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return invocation.getArguments()[1];
                    }
                });

        Mockito.when(mockFlatCache.getDouble(Mockito.anyString(), Mockito.any(Double.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });

        Mockito.when(mockFlatCache.getInt(Mockito.anyString(), Mockito.any(Integer.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });

        Mockito.when(mockFlatCache.getString(Mockito.anyString(), Mockito.any(String.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });
    }
}
