package io.liquorice.config.cache;

import io.liquorice.config.cache.file.SinglePropertiesFileCache;
import io.liquorice.config.cache.memory.BlackHoleCache;
import io.liquorice.config.cache.memory.KeyValueCache;
import io.liquorice.config.exception.cache.CacheWarmingException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

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
        if (propertiesUrl != null) {
            propertiesPath = Paths.get(propertiesUrl.getPath());
        }

        // Create mocks
        mockMiddleCache = mock(BlackHoleCache.class);
        mockTerminalCache = mock(BlackHoleCache.class);

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
        assertEquals(actualStringResult, EXISTING_STRING_VALUE, "Did not retrieve correct value for existing key:");
        double actualDoubleValue = keyValueFlatCache.getDouble(EXISTING_DOUBLE_KEY, DEFAULT_DOUBLE_VALUE);
        assertEquals(actualDoubleValue, EXISTING_DOUBLE_VALUE, "Did not retrieve correct value for existing key:");

        // Get same value as above (should be cached now)
        actualStringResult = keyValueFlatCache.getString(EXISTING_STRING_KEY, DEFAULT_STRING_VALUE);
        assertEquals(actualStringResult, EXISTING_STRING_VALUE,
                "Did not retrieve correct value for already cached key:");

        // Get value that doesn't exist in simpleFileCache nor keyValueFlatCache
        actualStringResult = keyValueFlatCache.getString("non-existent-value", DEFAULT_STRING_VALUE);
        assertEquals(actualStringResult, DEFAULT_STRING_VALUE,
                "Retrieved non-default value for non-existent key in cache hierarchy:");

        // Verify mockMiddleCache.getString() was only called twice
        verify(mockMiddleCache, times(2)).getString(anyString(), anyString());

        // Verify mockMiddleCache.getDouble() was only called once
        verify(mockMiddleCache, times(1)).getDouble(anyString(), anyDouble());

        // Verify mockTerminalCache.getString() was only called once
        verify(mockTerminalCache, times(1)).getString(anyString(), anyString());
    }

    @Test(expectedExceptions = CacheWarmingException.class)
    public void testBadWarmingPath() throws Exception {
        final Path mockPath = mock(Path.class);
        when(mockPath.toFile()).thenThrow(new RuntimeException("Uh oh."));

        simpleFileCache = new SinglePropertiesFileCache();
        simpleFileCache.warm(mockPath, "UTF-8");
    }

    private void initTerminalMocks() {
        when(mockTerminalCache.getDouble(anyString(), anyDouble())).thenAnswer(
                invocation -> invocation.getArguments()[1]);

        when(mockTerminalCache.getString(anyString(), anyString())).thenAnswer(
                invocation -> invocation.getArguments()[1]);
    }

    private void initMiddleMocks() {
        when(mockMiddleCache.getWriteThroughCache()).thenReturn(simpleFileCache);

        when(mockMiddleCache.getString(anyString(), anyString())).thenAnswer(invocation -> {
            Object args[] = invocation.getArguments();
            return mockMiddleCache.getWriteThroughCache().getString((String) args[0], (String) args[1]);
        });

        when(mockMiddleCache.getDouble(anyString(), anyDouble())).thenAnswer(invocation -> {
            Object args[] = invocation.getArguments();
            return mockMiddleCache.getWriteThroughCache().getDouble((String) args[0], (Double) args[1]);
        });
    }
}
