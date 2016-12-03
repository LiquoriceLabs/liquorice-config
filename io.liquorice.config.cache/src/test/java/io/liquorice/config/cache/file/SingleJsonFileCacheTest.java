package io.liquorice.config.cache.file;

import com.owlike.genson.Genson;
import io.liquorice.config.cache.CacheLayer;
import io.liquorice.config.cache.CacheProperties;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Created by mthorpe on 6/13/15.
 */
public class SingleJsonFileCacheTest {
    private static final String ENCODING = "UTF-8";

    private SingleJsonFileCache singleJsonFileCache;
    private CacheLayer mockCache;

    @BeforeTest
    public void setup() throws Exception {
        // Build genson object
        final Genson genson = new Genson();
        Map<String, Object> map = new HashMap<>();
        map.put(CacheProperties.BOOLEAN_KEY, CacheProperties.CUSTOM_BOOLEAN_VALUE);
        map.put(CacheProperties.DOUBLE_KEY, CacheProperties.CUSTOM_DOUBLE_VALUE);
        map.put(CacheProperties.INT_KEY, CacheProperties.CUSTOM_INT_VALUE);
        map.put(CacheProperties.STRING_KEY, CacheProperties.CUSTOM_STRING_VALUE);
        String fileContents = genson.serialize(map);

        // Setup mock data sources
        final InputStream inputStream = new ByteArrayInputStream(fileContents.getBytes(ENCODING));
        mockCache = mock(SinglePropertiesFileCache.class);
        singleJsonFileCache = new SingleJsonFileCache();
        singleJsonFileCache.setWriteThroughCache(mockCache);
        singleJsonFileCache.warm(inputStream, ENCODING);
        initDefaultAnswers();
    }

    @Test
    public void testGetBoolean() {
        boolean actualResult = singleJsonFileCache.getBoolean(CacheProperties.BOOLEAN_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_BOOLEAN_VALUE, "Existing key not found in cache:");

        actualResult = singleJsonFileCache.getBoolean(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualResult, CacheProperties.DEFAULT_BOOLEAN_VALUE, "Non-existent key found in cache:");

        verify(mockCache, times(1)).getBoolean(anyString(), anyBoolean());
    }

    @Test
    public void testGetDouble() {
        double actualResult = singleJsonFileCache.getDouble(CacheProperties.DOUBLE_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_DOUBLE_VALUE, "Existing key not found in cache:");

        actualResult = singleJsonFileCache.getDouble(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualResult, CacheProperties.DEFAULT_DOUBLE_VALUE, "Non-existent key found in cache:");

        verify(mockCache, times(1)).getDouble(anyString(), anyDouble());
    }

    @Test
    public void testGetInt() {
        int actualResult = singleJsonFileCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_INT_VALUE, "Existing key not found in cache:");

        actualResult = singleJsonFileCache.getInt(CacheProperties.NON_EXISTENT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualResult, CacheProperties.DEFAULT_INT_VALUE, "Non-existent key found in cache:");

        verify(mockCache, times(1)).getInt(anyString(), anyInt());
    }

    @Test
    public void testGetString() {
        String actualResult = singleJsonFileCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_STRING_VALUE, "Existing key not found in cache:");

        actualResult = singleJsonFileCache.getString(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualResult, CacheProperties.DEFAULT_STRING_VALUE, "Non-existent key found in cache:");

        verify(mockCache, times(1)).getString(anyString(), anyString());
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
