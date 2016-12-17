package io.liquorice.config.storage.file.properties;

import com.google.common.base.Charsets;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.function.Function;

import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DEFAULT_STRING_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOES_NOT_EXIST_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_VALUE;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Created by mthorpe on 12/11/16.
 */
public class ReadablePropertiesFileConfigSpaceTest {

    private ReadablePropertiesFileConfigSpace configSpace;

    @BeforeTest
    public void setup() throws Exception {
        // Initialize seed properties
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final Properties seedProperties = new Properties();
        seedProperties.setProperty(BOOL_KEY, Boolean.toString(BOOL_VALUE));
        seedProperties.setProperty(DOUBLE_KEY, Double.toString(DOUBLE_VALUE));
        seedProperties.setProperty(INT_KEY, Integer.toString(INT_VALUE));
        seedProperties.setProperty(LONG_KEY, Long.toString(LONG_VALUE));
        seedProperties.setProperty(STRING_KEY, STRING_VALUE);
        seedProperties.store(baos, null);
        final InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()),
                Charsets.UTF_8);

        final FileChannel mockFileChannel = mock(FileChannel.class);
        final Function<FileChannel, Reader> fileChannelReaderFunction = internalFileChannel -> isr;

        // Initialize sut
        final ConfigFormatter configFormatter = new PassThroughConfigFormatter();
        configSpace = new ReadablePropertiesFileConfigSpace.Builder() //
                .withConfigFormatter(configFormatter) //
                .withFileChannel(mockFileChannel) //
                .withFileChannelReaderFunction(fileChannelReaderFunction) //
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
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGettersReturnDefaultValues() {
        assertEquals(configSpace.getBoolean(DOES_NOT_EXIST_KEY, DEFAULT_BOOL_VALUE), DEFAULT_BOOL_VALUE);
        assertEquals(configSpace.getDouble(DOES_NOT_EXIST_KEY, DEFAULT_DOUBLE_VALUE), DEFAULT_DOUBLE_VALUE);
        assertEquals(configSpace.getInt(DOES_NOT_EXIST_KEY, DEFAULT_INT_VALUE), DEFAULT_INT_VALUE);
        assertEquals(configSpace.getLong(DOES_NOT_EXIST_KEY, DEFAULT_LONG_VALUE), DEFAULT_LONG_VALUE);
        assertEquals(configSpace.getString(DOES_NOT_EXIST_KEY, DEFAULT_STRING_VALUE), DEFAULT_STRING_VALUE);
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

    @SuppressWarnings("unchecked")
    @Test
    public void testPropertyOfWrongTypeReturnsDefaultValue() {
        assertEquals(configSpace.getBoolean(DOUBLE_KEY, DEFAULT_BOOL_VALUE), DEFAULT_BOOL_VALUE);
        assertEquals(configSpace.getDouble(BOOL_KEY, DEFAULT_DOUBLE_VALUE), DEFAULT_DOUBLE_VALUE);
        assertEquals(configSpace.getInt(BOOL_KEY, DEFAULT_INT_VALUE), DEFAULT_INT_VALUE);
        assertEquals(configSpace.getLong(BOOL_KEY, DEFAULT_LONG_VALUE), DEFAULT_LONG_VALUE);
    }

    @Test
    public void testRequiredBooleanPropertyOfWrongType() {
        assertFalse(configSpace.getBooleanRequired(DOUBLE_KEY));
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
}
