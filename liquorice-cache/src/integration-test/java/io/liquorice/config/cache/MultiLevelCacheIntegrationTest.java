package io.liquorice.config.cache;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.liquorice.config.cache.file.SinglePropertiesFileCache;
import io.liquorice.config.cache.memory.BlackHoleCache;
import io.liquorice.config.cache.memory.KeyValueCache;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mthorpe on 6/11/15.
 */
public class MultiLevelCacheIntegrationTest {
    private static final double DEFAULT_DOUBLE_VALUE = 0.0;
    private static final String DEFAULT_STRING_VALUE = "default value";
    private static final String EXISTING_DOUBLE_KEY = "test.double";
    private static final String EXISTING_STRING_KEY = "test.string";
    private static final double EXISTING_DOUBLE_VALUE = 1.5;
    private static final String EXISTING_STRING_VALUE = "test data";

    private CacheLayer mockMiddleCache;
    private CacheLayer mockTerminalCache;
    private CacheLayer simpleFileCache;
    private CacheLayer keyValueFlatCache;

    @Test
    public void testTwoLevelCachingForGet() throws Exception {
        Path propertiesPath = null;
        URL propertiesUrl = this.getClass().getClassLoader().getResource("simple-cache.properties");
        if(propertiesUrl != null) {
            propertiesPath = Paths.get(propertiesUrl.getPath());
        }

        // Create mocks
        mockMiddleCache = Mockito.mock(BlackHoleCache.class);
        mockTerminalCache = Mockito.mock(BlackHoleCache.class);

        // Setup real caches
        simpleFileCache = new SinglePropertiesFileCache();
        simpleFileCache.setWriteThroughCache(mockTerminalCache);
        simpleFileCache.warm(propertiesPath, "UTF-8");
        keyValueFlatCache = new KeyValueCache();
        keyValueFlatCache.setWriteThroughCache(mockMiddleCache);

        // Hook it all together
        initMiddleMocks();
        initTerminalMocks();

        // Get two values that exists in simpleFileCache, but not in keyValueFlatCache
        String actualStringResult = keyValueFlatCache.getString(EXISTING_STRING_KEY, DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualStringResult, EXISTING_STRING_VALUE,
                "Did not retrieve correct value for existing key:");
        double actualDoubleValue = keyValueFlatCache.getDouble(EXISTING_DOUBLE_KEY, DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualDoubleValue, EXISTING_DOUBLE_VALUE,
                "Did not retrieve correct value for existing key:");

        // Get same value as above (should be cached now)
        actualStringResult = keyValueFlatCache.getString(EXISTING_STRING_KEY, DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualStringResult, EXISTING_STRING_VALUE,
                "Did not retrieve correct value for already cached key:");

        // Get value that doesn't exist in simpleFileCache nor keyValueFlatCache
        actualStringResult = keyValueFlatCache.getString("non-existent-value", DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualStringResult, DEFAULT_STRING_VALUE,
                "Retrieved non-default value for non-existent key in cache hierarchy:");

        // Verify mockMiddleCache.getString() was only called twice
        Mockito.verify(mockMiddleCache, Mockito.times(2)).getString(Mockito.anyString(), Mockito.anyString());

        // Verify mockMiddleCache.getDouble() was only called once
        Mockito.verify(mockMiddleCache, Mockito.times(1)).getDouble(Mockito.anyString(), Mockito.anyDouble());

        // Verify mockTerminalCache.getString() was only called once
        Mockito.verify(mockTerminalCache, Mockito.times(1)).getString(Mockito.anyString(), Mockito.anyString());
    }

    private void initTerminalMocks() {
        Mockito.when(mockTerminalCache.getString(Mockito.anyString(), Mockito.anyString())).thenAnswer(
                new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        return args[1];
                    }
                });

        Mockito.when(mockTerminalCache.getString(Mockito.anyString(), Mockito.anyString())).thenAnswer(
                new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        return args[1];
                    }
                });
    }

    private void initMiddleMocks() {
        Mockito.when(mockMiddleCache.getWriteThroughCache()).thenReturn(simpleFileCache);

        Mockito.when(mockMiddleCache.getString(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object args[] = invocation.getArguments();
                return mockMiddleCache.getWriteThroughCache().getString((String) args[0], (String) args[1]);
            }
        });

        Mockito.when(mockMiddleCache.getDouble(Mockito.anyString(), Mockito.anyDouble())).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object args[] = invocation.getArguments();
                return mockMiddleCache.getWriteThroughCache().getDouble((String) args[0], (Double) args[1]);
            }
        });
    }
}
