package io.liquorice.core.cache.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.liquorice.core.cache.StorageCacheLayer;

/**
 * Created by mthorpe on 6/13/15.
 */
public class SinglePropertiesFileCacheTest {
    private static final String NON_EXISTENT_KEY = "non-existent-key";

    private static final String BOOLEAN_KEY = "test.boolean";
    private static final boolean CUSTOM_BOOLEAN_VALUE = true;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    private static final String DOUBLE_KEY = "test.double";
    private static final double CUSTOM_DOUBLE_VALUE = 1.5;
    private static final double DEFAULT_DOUBLE_VALUE = 1.0;

    private static final String INT_KEY = "test.int";
    private static final int CUSTOM_INT_VALUE = 10;
    private static final int DEFAULT_INT_VALUE = 4;

    private static final String STRING_KEY = "test.string";
    private static final String CUSTOM_STRING_VALUE = "test data";
    private static final String DEFAULT_STRING_VALUE = "default_value";

    private static final String ENCODING = "UTF-8";
    private static final String FILE_CONTENTS = BOOLEAN_KEY + "=" + CUSTOM_BOOLEAN_VALUE + "\n" + DOUBLE_KEY + "="
            + CUSTOM_DOUBLE_VALUE + "\n" + INT_KEY + "=" + CUSTOM_INT_VALUE + "\n" + STRING_KEY + "="
            + CUSTOM_STRING_VALUE + "\n";
    InputStream inputStream;
    SinglePropertiesFileCache singlePropertiesFileCache;
    StorageCacheLayer mockCache;

    @BeforeTest
    public void setup() throws Exception {
        inputStream = new ByteArrayInputStream(FILE_CONTENTS.getBytes());
        mockCache = Mockito.mock(SinglePropertiesFileCache.class);
        singlePropertiesFileCache = new SinglePropertiesFileCache();
        singlePropertiesFileCache.setWriteThroughCache(mockCache);
        singlePropertiesFileCache.warm(inputStream, ENCODING);
        initDefaultAnswers();
    }

    @Test
    public void testGetBoolean() {
        boolean actualResult = singlePropertiesFileCache.getBoolean(BOOLEAN_KEY, DEFAULT_BOOLEAN_VALUE);
        Assert.assertEquals(actualResult, CUSTOM_BOOLEAN_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getBoolean(NON_EXISTENT_KEY, DEFAULT_BOOLEAN_VALUE);
        Assert.assertEquals(actualResult, DEFAULT_BOOLEAN_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getBoolean(Mockito.anyString(), Mockito.anyBoolean());
    }

    @Test
    public void testGetDouble() {
        double actualResult = singlePropertiesFileCache.getDouble(DOUBLE_KEY, DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualResult, CUSTOM_DOUBLE_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getDouble(NON_EXISTENT_KEY, DEFAULT_DOUBLE_VALUE);
        Assert.assertEquals(actualResult, DEFAULT_DOUBLE_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getDouble(Mockito.anyString(), Mockito.anyDouble());
    }

    @Test
    public void testGetInt() {
        int actualResult = singlePropertiesFileCache.getInt(INT_KEY, DEFAULT_INT_VALUE);
        Assert.assertEquals(actualResult, CUSTOM_INT_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getInt(NON_EXISTENT_KEY, DEFAULT_INT_VALUE);
        Assert.assertEquals(actualResult, DEFAULT_INT_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getInt(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void testGetString() {
        String actualResult = singlePropertiesFileCache.getString(STRING_KEY, DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualResult, CUSTOM_STRING_VALUE, "Existing key not found in cache:");

        actualResult = singlePropertiesFileCache.getString(NON_EXISTENT_KEY, DEFAULT_STRING_VALUE);
        Assert.assertEquals(actualResult, DEFAULT_STRING_VALUE, "Non-existent key found in cache:");

        Mockito.verify(mockCache, Mockito.times(1)).getString(Mockito.anyString(), Mockito.anyString());
    }

    private void initDefaultAnswers() {
        Mockito.when(mockCache.getBoolean(Mockito.anyString(), Mockito.any(Boolean.class))).thenAnswer(
                new Answer() {
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
