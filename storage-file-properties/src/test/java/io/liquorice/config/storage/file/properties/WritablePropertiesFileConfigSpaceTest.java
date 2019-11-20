package io.liquorice.config.storage.file.properties;

import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_STRING_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;

/**
 * Created by mthorpe on 12/13/16.
 */
@ExtendWith(MockitoExtension.class)
class WritablePropertiesFileConfigSpaceTest {

    private static WritablePropertiesFileConfigSpace createConfigSpace(final OutputStream outputStream,
            final FileChannel fileChannel) throws Exception {
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
                StandardCharsets.UTF_8);
        final OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        final Function<FileChannel, Reader> fileChannelReaderFunction = internalFileChannel -> isr;
        final Function<FileChannel, Writer> fileChannelWriterFunction = internalFileChannel -> osw;

        // Initialize sut
        final ConfigFormatter configFormatter = new PassThroughConfigFormatter();
        return new WritablePropertiesFileConfigSpace.Builder() //
                .withConfigFormatter(configFormatter) //
                .withFileChannel(fileChannel) //
                .withFileChannelReaderFunction(fileChannelReaderFunction) //
                .withFileChannelWriterFunction(fileChannelWriterFunction) //
                .build();
    }

    @Mock
    private FileChannel mockFileChannel;

    @Test
    void testUpdateBoolean() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setBoolean(BOOL_KEY, UPDATED_BOOL_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_BOOL_VALUE, configSpace.getBooleanRequired(BOOL_KEY));

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(UPDATED_BOOL_VALUE, Boolean.parseBoolean(properties.getProperty(BOOL_KEY)));
        assertEquals(DOUBLE_VALUE, Double.parseDouble(properties.getProperty(DOUBLE_KEY)));
        assertEquals(INT_VALUE, Integer.parseInt(properties.getProperty(INT_KEY)));
        assertEquals(LONG_VALUE, Long.parseLong(properties.getProperty(LONG_KEY)));
        assertEquals(STRING_VALUE, properties.getProperty(STRING_KEY));
    }

    @Test
    void testUpdateDouble() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setDouble(DOUBLE_KEY, UPDATED_DOUBLE_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_DOUBLE_VALUE, configSpace.getDoubleRequired(DOUBLE_KEY));

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, Boolean.parseBoolean(properties.getProperty(BOOL_KEY)));
        assertEquals(UPDATED_DOUBLE_VALUE, Double.parseDouble(properties.getProperty(DOUBLE_KEY)));
        assertEquals(INT_VALUE, Integer.parseInt(properties.getProperty(INT_KEY)));
        assertEquals(LONG_VALUE, Long.parseLong(properties.getProperty(LONG_KEY)));
        assertEquals(STRING_VALUE, properties.getProperty(STRING_KEY));
    }

    @Test
    void testUpdateInt() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setInt(INT_KEY, UPDATED_INT_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_INT_VALUE, configSpace.getIntRequired(INT_KEY));

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, Boolean.parseBoolean(properties.getProperty(BOOL_KEY)));
        assertEquals(DOUBLE_VALUE, Double.parseDouble(properties.getProperty(DOUBLE_KEY)));
        assertEquals(UPDATED_INT_VALUE, Integer.parseInt(properties.getProperty(INT_KEY)));
        assertEquals(LONG_VALUE, Long.parseLong(properties.getProperty(LONG_KEY)));
        assertEquals(STRING_VALUE, properties.getProperty(STRING_KEY));
    }

    @Test
    void testUpdateLong() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setLong(LONG_KEY, UPDATED_LONG_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_LONG_VALUE, configSpace.getLongRequired(LONG_KEY));

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, Boolean.parseBoolean(properties.getProperty(BOOL_KEY)));
        assertEquals(DOUBLE_VALUE, Double.parseDouble(properties.getProperty(DOUBLE_KEY)));
        assertEquals(INT_VALUE, Integer.parseInt(properties.getProperty(INT_KEY)));
        assertEquals(UPDATED_LONG_VALUE, Long.parseLong(properties.getProperty(LONG_KEY)));
        assertEquals(STRING_VALUE, properties.getProperty(STRING_KEY));
    }

    @Test
    void testUpdateString() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setString(STRING_KEY, UPDATED_STRING_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_STRING_VALUE, configSpace.getStringRequired(STRING_KEY));

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, Boolean.parseBoolean(properties.getProperty(BOOL_KEY)));
        assertEquals(DOUBLE_VALUE, Double.parseDouble(properties.getProperty(DOUBLE_KEY)));
        assertEquals(INT_VALUE, Integer.parseInt(properties.getProperty(INT_KEY)));
        assertEquals(LONG_VALUE, Long.parseLong(properties.getProperty(LONG_KEY)));
        assertEquals(UPDATED_STRING_VALUE, properties.getProperty(STRING_KEY));
    }

    @Test
    void testRemoveProperty() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.remove(BOOL_KEY);

        // Verify the cache properties were updated
        assertFalse(configSpace.hasValue(BOOL_KEY));

        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertFalse(properties.containsKey(BOOL_KEY));
        assertEquals(DOUBLE_VALUE, Double.parseDouble(properties.getProperty(DOUBLE_KEY)));
        assertEquals(INT_VALUE, Integer.parseInt(properties.getProperty(INT_KEY)));
        assertEquals(LONG_VALUE, Long.parseLong(properties.getProperty(LONG_KEY)));
        assertEquals(STRING_VALUE, properties.getProperty(STRING_KEY));
    }
}
