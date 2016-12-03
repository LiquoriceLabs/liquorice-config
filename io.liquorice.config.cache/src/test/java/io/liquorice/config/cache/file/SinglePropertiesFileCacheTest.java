package io.liquorice.config.cache.file;

import io.liquorice.config.cache.CacheLayer;
import io.liquorice.config.cache.CacheProperties;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
public class SinglePropertiesFileCacheTest {
    private static final String ENCODING = "UTF-8";
    private static final String FILE_CONTENTS = CacheProperties.BOOLEAN_KEY + "="
            + CacheProperties.CUSTOM_BOOLEAN_VALUE + "\n" + CacheProperties.DOUBLE_KEY + "="
            + CacheProperties.CUSTOM_DOUBLE_VALUE + "\n" + CacheProperties.INT_KEY + "="
            + CacheProperties.CUSTOM_INT_VALUE + "\n" + CacheProperties.STRING_KEY + "="
            + CacheProperties.CUSTOM_STRING_VALUE + "\n";

    private SinglePropertiesFileCache singlePropertiesFileCache;
    private CacheLayer mockCache;

    @BeforeTest
    public void setup() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(FILE_CONTENTS.getBytes(ENCODING));
        mockCache = mock(SinglePropertiesFileCache.class);
        singlePropertiesFileCache = new SinglePropertiesFileCache();
        singlePropertiesFileCache.setWriteThroughCache(mockCache);
        singlePropertiesFileCache.warm(inputStream, ENCODING);
        initDefaultAnswers();
    }

    @Test
    public void testGetBoolean() {
        boolean actualResult = singlePropertiesFileCache.getBoolean(CacheProperties.BOOLEAN_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_BOOLEAN_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getBoolean(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        assertEquals(actualResult, CacheProperties.DEFAULT_BOOLEAN_VALUE, "Non-existent key found in cache:");

        verify(mockCache, times(1)).getBoolean(anyString(), anyBoolean());
    }

    @Test
    public void testGetDouble() {
        double actualResult = singlePropertiesFileCache.getDouble(CacheProperties.DOUBLE_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_DOUBLE_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getDouble(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        assertEquals(actualResult, CacheProperties.DEFAULT_DOUBLE_VALUE, "Non-existent key found in cache:");

        verify(mockCache, times(1)).getDouble(anyString(), anyDouble());
    }

    @Test
    public void testGetInt() {
        int actualResult = singlePropertiesFileCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_INT_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getInt(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_INT_VALUE);
        assertEquals(actualResult, CacheProperties.DEFAULT_INT_VALUE, "Non-existent key found in cache:");

        verify(mockCache, times(1)).getInt(anyString(), anyInt());
    }

    @Test
    public void testGetString() {
        String actualResult = singlePropertiesFileCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        assertEquals(actualResult, CacheProperties.CUSTOM_STRING_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getString(CacheProperties.NON_EXISTENT_KEY,
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
