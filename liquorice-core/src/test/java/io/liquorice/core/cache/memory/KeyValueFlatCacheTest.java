package io.liquorice.core.cache.memory;

import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.liquorice.core.cache.exception.CacheInitializationException;

/**
 * Created by mthorpe on 6/10/15.
 */
public class KeyValueFlatCacheTest {
    private static final boolean CUSTOM_BOOLEAN = false;
    private static final double CUSTOM_DOUBLE = 1.5;
    private static final int CUSTOM_INT = 1;
    private static final String CUSTOM_STRING = "test_value";

    private static final boolean DEFAULT_BOOLEAN = true;
    private static final double DEFAULT_DOUBLE = 0.0;
    private static final int DEFAULT_INT = 0;
    private static final String DEFAULT_STRING = "default_value";

    private static final String TEST_KEY_1 = "test.key.1";
    private static final String TEST_KEY_2 = "test.key.2";

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
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_INT);
        keyValueFlatCache.put(TEST_KEY_2, CUSTOM_STRING);
        keyValueFlatCache.clear();

        int actualIntValue = keyValueFlatCache.getInt(TEST_KEY_1, DEFAULT_INT);
        Assert.assertEquals(actualIntValue, DEFAULT_INT,
                "Cleared cache, but it still contains non-default int value(s)");
        String actualStringValue = keyValueFlatCache.getString(TEST_KEY_2, DEFAULT_STRING);
        Assert.assertEquals(actualStringValue, DEFAULT_STRING,
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
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_BOOLEAN);
        boolean actualValue = keyValueFlatCache.getBoolean(TEST_KEY_1, DEFAULT_BOOLEAN);
        Assert.assertEquals(actualValue, CUSTOM_BOOLEAN, "Failed to retrieve saved value from cache");

        // Retrieve boolean without storing
        actualValue = keyValueFlatCache.getBoolean("other.key", DEFAULT_BOOLEAN);
        Assert.assertEquals(actualValue, DEFAULT_BOOLEAN, "Retrieved value from cache for unstored key");

        // Store string, retrieve boolean
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_STRING);
        actualValue = keyValueFlatCache.getBoolean(TEST_KEY_1, DEFAULT_BOOLEAN);
        Assert.assertEquals(actualValue, DEFAULT_BOOLEAN,
                "Stored a non-boolean item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetBooleanOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getBoolean(TEST_KEY_1, DEFAULT_BOOLEAN);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testGetDouble() throws Exception {
        // Store double, retrieve double
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_DOUBLE);
        double actualValue = keyValueFlatCache.getDouble(TEST_KEY_1, DEFAULT_DOUBLE);
        Assert.assertEquals(actualValue, CUSTOM_DOUBLE, "Failed to retrieve saved value from cache");

        // Retrieve double without storing
        actualValue = keyValueFlatCache.getDouble("other.key", DEFAULT_DOUBLE);
        Assert.assertEquals(actualValue, DEFAULT_DOUBLE, "Retrieved value from cache for unstored key");

        // Store string, retrieve double
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_STRING);
        actualValue = keyValueFlatCache.getDouble(TEST_KEY_1, DEFAULT_DOUBLE);
        Assert.assertEquals(actualValue, DEFAULT_DOUBLE,
                "Stored a non-double item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetDoubleOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getDouble(TEST_KEY_1, DEFAULT_DOUBLE);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testGetInt() throws Exception {
        // Store int, retrieve int
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_INT);
        int actualValue = keyValueFlatCache.getInt(TEST_KEY_1, DEFAULT_INT);
        Assert.assertEquals(actualValue, CUSTOM_INT, "Failed to retrieve saved value from cache");

        // Retrieve int without storing
        actualValue = keyValueFlatCache.getInt("other.key", DEFAULT_INT);
        Assert.assertEquals(actualValue, DEFAULT_INT, "Retrieved value from cache for unstored key");

        // Store string, retrieve int
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_STRING);
        actualValue = keyValueFlatCache.getInt(TEST_KEY_1, DEFAULT_INT);
        Assert.assertEquals(actualValue, DEFAULT_INT,
                "Stored a non-int item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetIntOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getInt(TEST_KEY_1, DEFAULT_INT);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testGetString() throws Exception {
        // Store string, retrieve string
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_STRING);
        String actualValue = keyValueFlatCache.getString(TEST_KEY_1, DEFAULT_STRING);
        Assert.assertEquals(actualValue, CUSTOM_STRING, "Failed to retrieve saved value from cache");

        // Retrieve string without storing
        actualValue = keyValueFlatCache.getString("other.key", DEFAULT_STRING);
        Assert.assertEquals(actualValue, DEFAULT_STRING, "Retrieved value from cache for unstored key");

        // Store boolean, retrieve string
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_BOOLEAN);
        actualValue = keyValueFlatCache.getString(TEST_KEY_1, DEFAULT_STRING);
        Assert.assertEquals(actualValue, DEFAULT_STRING,
                "Stored a non-string item in the cache, but retrieved a non-default value");
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testGetStringOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.getString(TEST_KEY_1, DEFAULT_STRING);
        Assert.fail("Uninitialized cache failed to throw exception");
    }

    @Test
    public void testInvalidate() {
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_INT);
        keyValueFlatCache.put(TEST_KEY_2, CUSTOM_STRING);

        // Invalidate an item
        Object invalidatedValue = keyValueFlatCache.invalidate(TEST_KEY_1);
        Assert.assertTrue(invalidatedValue instanceof Integer);
        int invalidatedInt = (Integer) invalidatedValue;
        Assert.assertEquals(invalidatedInt, CUSTOM_INT, "Removed item did not return the saved value");

        // Verify item was invalidated
        int actualIntValue = keyValueFlatCache.getInt(TEST_KEY_1, DEFAULT_INT);
        Assert.assertEquals(actualIntValue, DEFAULT_INT, "Invalidated item was not cleared from the cache");

        // Verify other items were not invalidated
        String actualStringValue = keyValueFlatCache.getString(TEST_KEY_2, DEFAULT_STRING);
        Assert.assertEquals(actualStringValue, CUSTOM_STRING, "Non-invalidated item no longer exists in the cache");
    }

    @Test
    public void testPutAllAndFlush() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(TEST_KEY_1, CUSTOM_INT);
        map.put(TEST_KEY_2, CUSTOM_STRING);

        // Dump contents into cache
        keyValueFlatCache.putAll(map);

        // Verify values
        int actualIntValue = keyValueFlatCache.getInt(TEST_KEY_1, DEFAULT_INT);
        Assert.assertEquals(actualIntValue, CUSTOM_INT, "Failed to retrieve saved value from cache");
        String actualStringValue = keyValueFlatCache.getString(TEST_KEY_2, DEFAULT_STRING);
        Assert.assertEquals(actualStringValue, CUSTOM_STRING, "Failed to retrieve saved value from cache");

        // Verify flush
        keyValueFlatCache.flush();
        Mockito.verify(mockFlatCache, Mockito.times(1)).putAll(Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void testRemove() {
        keyValueFlatCache.put(TEST_KEY_1, CUSTOM_INT);
        keyValueFlatCache.put(TEST_KEY_2, CUSTOM_STRING);

        // Remove an item
        Object invalidatedValue = keyValueFlatCache.remove(TEST_KEY_1);
        Assert.assertTrue(invalidatedValue instanceof Integer);
        int invalidatedInt = (Integer) invalidatedValue;
        Assert.assertEquals(invalidatedInt, CUSTOM_INT, "Removed item did not return the saved value");

        // Verify item was removed
        int actualIntValue = keyValueFlatCache.getInt(TEST_KEY_1, DEFAULT_INT);
        Assert.assertEquals(actualIntValue, DEFAULT_INT, "Removed item was not cleared from the cache");

        // Verify other items were not removed
        String actualStringValue = keyValueFlatCache.getString(TEST_KEY_2, DEFAULT_STRING);
        Assert.assertEquals(actualStringValue, CUSTOM_STRING, "Non-removed item no longer exists in the cache");

        // Verify we attempted to remove the item from the write through cache too
        Mockito.verify(mockFlatCache, Mockito.times(1)).remove(Mockito.anyString());
    }

    @Test(expectedExceptions = CacheInitializationException.class)
    public void testRemoveOnUninitializedCache() {
        KeyValueFlatCache uninitializedCache = new KeyValueFlatCache();
        uninitializedCache.remove(TEST_KEY_1);
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
