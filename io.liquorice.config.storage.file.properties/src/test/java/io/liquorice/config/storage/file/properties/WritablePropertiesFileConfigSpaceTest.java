package io.liquorice.config.storage.file.properties;

import com.google.common.base.Charsets;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.function.Function;

import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.BOOL_VALUE;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.DOUBLE_KEY;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.DOUBLE_VALUE;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.INT_KEY;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.INT_VALUE;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.LONG_KEY;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.LONG_VALUE;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.STRING_KEY;
import static io.liquorice.config.storage.file.properties.ConfigSpaceTestData.STRING_VALUE;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

/**
 * Created by mthorpe on 12/13/16.
 */
public class WritablePropertiesFileConfigSpaceTest {

    private static final boolean UPDATED_BOOL_VALUE = false;
    private static final double UPDATED_DOUBLE_VALUE = 14.5;
    private static final int UPDATED_INT_VALUE = 9;
    private static final long UPDATED_LONG_VALUE = 31L;
    private static final String UPDATED_STRING_VALUE = "updated string";

    @Test
    public void testUpdateBoolean() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setBoolean(BOOL_KEY, UPDATED_BOOL_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getBooleanRequired(BOOL_KEY), UPDATED_BOOL_VALUE);

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(Boolean.parseBoolean(properties.getProperty(BOOL_KEY)), UPDATED_BOOL_VALUE);
        assertEquals(Double.parseDouble(properties.getProperty(DOUBLE_KEY)), DOUBLE_VALUE);
        assertEquals(Integer.parseInt(properties.getProperty(INT_KEY)), INT_VALUE);
        assertEquals(Long.parseLong(properties.getProperty(LONG_KEY)), LONG_VALUE);
        assertEquals(properties.getProperty(STRING_KEY), STRING_VALUE);
    }

    @Test
    public void testUpdateDouble() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setDouble(DOUBLE_KEY, UPDATED_DOUBLE_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getDoubleRequired(DOUBLE_KEY), UPDATED_DOUBLE_VALUE);

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(Boolean.parseBoolean(properties.getProperty(BOOL_KEY)), BOOL_VALUE);
        assertEquals(Double.parseDouble(properties.getProperty(DOUBLE_KEY)), UPDATED_DOUBLE_VALUE);
        assertEquals(Integer.parseInt(properties.getProperty(INT_KEY)), INT_VALUE);
        assertEquals(Long.parseLong(properties.getProperty(LONG_KEY)), LONG_VALUE);
        assertEquals(properties.getProperty(STRING_KEY), STRING_VALUE);
    }

    @Test
    public void testUpdateInt() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setInt(INT_KEY, UPDATED_INT_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getIntRequired(INT_KEY), UPDATED_INT_VALUE);

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(Boolean.parseBoolean(properties.getProperty(BOOL_KEY)), BOOL_VALUE);
        assertEquals(Double.parseDouble(properties.getProperty(DOUBLE_KEY)), DOUBLE_VALUE);
        assertEquals(Integer.parseInt(properties.getProperty(INT_KEY)), UPDATED_INT_VALUE);
        assertEquals(Long.parseLong(properties.getProperty(LONG_KEY)), LONG_VALUE);
        assertEquals(properties.getProperty(STRING_KEY), STRING_VALUE);
    }

    @Test
    public void testUpdateLong() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setLong(LONG_KEY, UPDATED_LONG_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getLongRequired(LONG_KEY), UPDATED_LONG_VALUE);

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(Boolean.parseBoolean(properties.getProperty(BOOL_KEY)), BOOL_VALUE);
        assertEquals(Double.parseDouble(properties.getProperty(DOUBLE_KEY)), DOUBLE_VALUE);
        assertEquals(Integer.parseInt(properties.getProperty(INT_KEY)), INT_VALUE);
        assertEquals(Long.parseLong(properties.getProperty(LONG_KEY)), UPDATED_LONG_VALUE);
        assertEquals(properties.getProperty(STRING_KEY), STRING_VALUE);
    }

    @Test
    public void testUpdateString() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritablePropertiesFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setString(STRING_KEY, UPDATED_STRING_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getStringRequired(STRING_KEY), UPDATED_STRING_VALUE);

        // Verify the on disk properties were updated
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(Boolean.parseBoolean(properties.getProperty(BOOL_KEY)), BOOL_VALUE);
        assertEquals(Double.parseDouble(properties.getProperty(DOUBLE_KEY)), DOUBLE_VALUE);
        assertEquals(Integer.parseInt(properties.getProperty(INT_KEY)), INT_VALUE);
        assertEquals(Long.parseLong(properties.getProperty(LONG_KEY)), LONG_VALUE);
        assertEquals(properties.getProperty(STRING_KEY), UPDATED_STRING_VALUE);
    }

    private static WritablePropertiesFileConfigSpace createConfigSpace(final OutputStream outputStream) throws Exception {
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
        final OutputStreamWriter osw = new OutputStreamWriter(outputStream, Charsets.UTF_8);

        final FileChannel mockFileChannel = mock(FileChannel.class);
        final Function<FileChannel, Reader> fileChannelReaderFunction = internalFileChannel -> isr;
        final Function<FileChannel, Writer> fileChannelWriterFunction = internalFileChannel -> osw;

        // Initialize sut
        final ConfigFormatter configFormatter = new PassThroughConfigFormatter();
        return new WritablePropertiesFileConfigSpace.Builder() //
                .withConfigFormatter(configFormatter) //
                .withFileChannel(mockFileChannel) //
                .withFileChannelReaderFunction(fileChannelReaderFunction) //
                .withFileChannelWriterFunction(fileChannelWriterFunction) //
                .build();
    }
}
