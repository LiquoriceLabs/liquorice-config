package io.liquorice.config.cache.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.liquorice.config.cache.CacheLayer;
import io.liquorice.config.cache.CacheProperties;

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

    private InputStream inputStream;
    private SinglePropertiesFileCache singlePropertiesFileCache;
    private CacheLayer mockCache;

    @BeforeTest
    public void setup() throws Exception {
        inputStream = new ByteArrayInputStream(FILE_CONTENTS.getBytes(ENCODING));
        mockCache = Mockito.mock(SinglePropertiesFileCache.class);
        singlePropertiesFileCache = new SinglePropertiesFileCache();
        singlePropertiesFileCache.setWriteThroughCache(mockCache);
        singlePropertiesFileCache.warm(inputStream, ENCODING);
        initDefaultAnswers();
    }

    @Test
    public void testGetBoolean() {
        boolean actualResult = singlePropertiesFileCache.getBoolean(CacheProperties.BOOLEAN_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.CUSTOM_BOOLEAN_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getBoolean(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_BOOLEAN_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.DEFAULT_BOOLEAN_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getBoolean(Mockito.anyString(), Mockito.anyBoolean());
    }

    @Test
    public void testGetDouble() {
        double actualResult = singlePropertiesFileCache.getDouble(CacheProperties.DOUBLE_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.CUSTOM_DOUBLE_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getDouble(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.DEFAULT_DOUBLE_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getDouble(Mockito.anyString(), Mockito.anyDouble());
    }

    @Test
    public void testGetInt() {
        int actualResult = singlePropertiesFileCache.getInt(CacheProperties.INT_KEY, CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.CUSTOM_INT_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getInt(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_INT_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.DEFAULT_INT_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getInt(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void testGetString() {
        String actualResult = singlePropertiesFileCache.getString(CacheProperties.STRING_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.CUSTOM_STRING_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getString(CacheProperties.NON_EXISTENT_KEY,
                CacheProperties.DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualResult, CacheProperties.DEFAULT_STRING_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getString(Mockito.anyString(), Mockito.anyString());
    }

    private void initDefaultAnswers() {
        Mockito.when(mockCache.getBoolean(Mockito.anyString(), Mockito.any(Boolean.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });

        Mockito.when(mockCache.getDouble(Mockito.anyString(), Mockito.any(Double.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });

        Mockito.when(mockCache.getInt(Mockito.anyString(), Mockito.any(Integer.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });

        Mockito.when(mockCache.getString(Mockito.anyString(), Mockito.any(String.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });
    }
}
