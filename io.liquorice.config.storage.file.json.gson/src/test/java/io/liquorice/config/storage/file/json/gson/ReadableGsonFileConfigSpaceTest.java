package io.liquorice.config.storage.file.json.gson;

import com.google.common.base.Charsets;
import com.google.gson.GsonBuilder;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.formatter.json.gson.GsonConfigFormatter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.function.Function;

import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.COMPLEX_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.COMPLEX_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_COMPLEX_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_STRING_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOES_NOT_EXIST_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.JSON_STRING;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_VALUE;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by mthorpe on 12/11/16.
 */
public class ReadableGsonFileConfigSpaceTest {

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    private ReadableGsonFileConfigSpace configSpace;

    @BeforeTest
    public void setup() throws Exception {
        // Initialize seed properties
        final InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(
                JSON_STRING.getBytes(Charsets.UTF_8)), Charsets.UTF_8);

        final FileChannel mockFileChannel = mock(FileChannel.class);
        final Function<FileChannel, Reader> fileChannelReaderFunction = internalFileChannel -> isr;

        // Initialize sut
        final ConfigFormatter configFormatter = new GsonConfigFormatter.Builder().build();
        configSpace = new ReadableGsonFileConfigSpace.Builder() //
                .withConfigFormatter(configFormatter) //
                .withFileChannel(mockFileChannel) //
                .withFileChannelReaderFunction(fileChannelReaderFunction) //
                .withGsonBuilder(GSON_BUILDER) //
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGettersReturnCorrectValues() {
        assertEquals(configSpace.getBoolean(BOOL_KEY, DEFAULT_BOOL_VALUE), BOOL_VALUE);
        assertEquals(configSpace.getDouble(DOUBLE_KEY, DEFAULT_DOUBLE_VALUE), DOUBLE_VALUE);
        assertEquals(configSpace.getInt(INT_KEY, DEFAULT_INT_VALUE), INT_VALUE);
        assertEquals(configSpace.getLong(LONG_KEY, DEFAULT_LONG_VALUE), LONG_VALUE);
        assertEquals(configSpace.getString(STRING_KEY, DEFAULT_STRING_VALUE), STRING_VALUE);

        final List<String> actualComplexValue = configSpace.getObject(COMPLEX_KEY, DEFAULT_COMPLEX_VALUE, List.class);
        assertEquals(actualComplexValue.size(), COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGettersReturnDefaultValues() {
        assertEquals(configSpace.getBoolean(DOES_NOT_EXIST_KEY, DEFAULT_BOOL_VALUE), DEFAULT_BOOL_VALUE);
        assertEquals(configSpace.getDouble(DOES_NOT_EXIST_KEY, DEFAULT_DOUBLE_VALUE), DEFAULT_DOUBLE_VALUE);
        assertEquals(configSpace.getInt(DOES_NOT_EXIST_KEY, DEFAULT_INT_VALUE), DEFAULT_INT_VALUE);
        assertEquals(configSpace.getLong(DOES_NOT_EXIST_KEY, DEFAULT_LONG_VALUE), DEFAULT_LONG_VALUE);
        assertEquals(configSpace.getString(DOES_NOT_EXIST_KEY, DEFAULT_STRING_VALUE), DEFAULT_STRING_VALUE);

        final List<String> actualComplexValue = configSpace.getObject(DOES_NOT_EXIST_KEY, DEFAULT_COMPLEX_VALUE,
                List.class);
        assertEquals(actualComplexValue.size(), DEFAULT_COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(DEFAULT_COMPLEX_VALUE));
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredBooleanPropertyDoesNotExist() {
        configSpace.getBooleanRequired(DOES_NOT_EXIST_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredDoublePropertyDoesNotExist() {
        configSpace.getDoubleRequired(DOES_NOT_EXIST_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredIntegerPropertyDoesNotExist() {
        configSpace.getIntRequired(DOES_NOT_EXIST_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredLongPropertyDoesNotExist() {
        configSpace.getLongRequired(DOES_NOT_EXIST_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredStringPropertyDoesNotExist() {
        configSpace.getStringRequired(DOES_NOT_EXIST_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredObjectPropertyDoesNotExist() {
        configSpace.getObjectRequired(DOES_NOT_EXIST_KEY, List.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPropertyOfWrongTypeReturnsDefaultValue() {
        assertEquals(configSpace.getBoolean(DOUBLE_KEY, DEFAULT_BOOL_VALUE), DEFAULT_BOOL_VALUE);
        assertEquals(configSpace.getDouble(BOOL_KEY, DEFAULT_DOUBLE_VALUE), DEFAULT_DOUBLE_VALUE);
        assertEquals(configSpace.getInt(BOOL_KEY, DEFAULT_INT_VALUE), DEFAULT_INT_VALUE);
        assertEquals(configSpace.getLong(BOOL_KEY, DEFAULT_LONG_VALUE), DEFAULT_LONG_VALUE);
        assertEquals(configSpace.getObject(BOOL_KEY, DEFAULT_COMPLEX_VALUE, List.class), DEFAULT_COMPLEX_VALUE);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredBooleanPropertyOfWrongType() {
        configSpace.getBooleanRequired(DOUBLE_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredDoublePropertyOfWrongType() {
        configSpace.getDoubleRequired(BOOL_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredIntegerPropertyOfWrongType() {
        configSpace.getIntRequired(BOOL_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredLongPropertyOfWrongType() {
        configSpace.getLongRequired(BOOL_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredObjectPropertyOfWrongType() {
        configSpace.getObjectRequired(BOOL_KEY, List.class);
    }
}
