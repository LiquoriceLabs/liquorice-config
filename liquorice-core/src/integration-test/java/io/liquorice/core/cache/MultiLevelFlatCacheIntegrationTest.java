package io.liquorice.core.cache;

import io.liquorice.core.cache.file.PropertiesFileCache;
import io.liquorice.core.cache.memory.BlackHoleFlatCache;
import io.liquorice.core.cache.memory.KeyValueFlatCache;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Paths;

/**
 * Created by mthorpe on 6/11/15.
 */
public class MultiLevelFlatCacheIntegrationTest {
    private static final String DEFAULT_VALUE = "default value";

    private FlatCacheLayer mockCache;

    @BeforeTest
    public void setup() {
        mockCache = Mockito.mock(BlackHoleFlatCache.class);

        initTerminalMocks();
    }

    @Test
    public void testTwoLevelCaching() {
        StorageCacheLayer simpleFileCache = new PropertiesFileCache();
        simpleFileCache.setWriteThroughCache(mockCache);
        simpleFileCache.warm(Paths.get("simple-cache.properties"));

        FlatCacheLayer keyValueFlatCache = new KeyValueFlatCache();
        keyValueFlatCache.setWriteThroughCache(simpleFileCache);

        // TODO: Get value that exists in simpleFileCache, but not in keyValueFlatCache


        // TODO: Get same value as above


        // TODO: Get value that doesn't exist in simpleFileCache nor keyValueFlatCache
        String actualResult = keyValueFlatCache.getString("non-existent-value", DEFAULT_VALUE);
        Assert.assertEquals(actualResult, DEFAULT_VALUE, "Retrieved non-default value for non-existent key in cache hierarchy");

        // TODO: Verify mockCache.getString() was only called once
        Mockito.verify(mockCache, Mockito.times(1)).getString(Mockito.anyString(), Mockito.anyString());
    }

    private void initTerminalMocks() {
        Mockito.when(mockCache.getString(Mockito.anyString(), Mockito.anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return args[1];
            }
        });
    }
}
