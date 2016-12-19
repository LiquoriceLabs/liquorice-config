package io.liquorice.config.storage.file.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.formatter.json.jackson.JacksonConfigFormatter;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.function.Function;

import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.COMPLEX_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.COMPLEX_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.JSON_STRING;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_COMPLEX_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_INT_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.UPDATED_STRING_VALUE;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by mthorpe on 12/13/16.
 */
public class WritableJacksonFileConfigSpaceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static WritableJacksonFileConfigSpace createConfigSpace(final OutputStream outputStream) throws Exception {
        // Initialize seed properties
        final InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(
                JSON_STRING.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        final OutputStreamWriter osw = new OutputStreamWriter(outputStream, Charsets.UTF_8);

        final FileChannel mockFileChannel = mock(FileChannel.class);
        final Function<FileChannel, Reader> fileChannelReaderFunction = internalFileChannel -> isr;
        final Function<FileChannel, Writer> fileChannelWriterFunction = internalFileChannel -> osw;

        // Initialize sut
        final ConfigFormatter configFormatter = new JacksonConfigFormatter.Builder().build();
        return new WritableJacksonFileConfigSpace.Builder() //
                .withConfigFormatter(configFormatter) //
                .withFileChannel(mockFileChannel) //
                .withFileChannelReaderFunction(fileChannelReaderFunction) //
                .withFileChannelWriterFunction(fileChannelWriterFunction) //
                .withObjectMapper(OBJECT_MAPPER) //
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateBoolean() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setBoolean(BOOL_KEY, UPDATED_BOOL_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getBooleanRequired(BOOL_KEY), UPDATED_BOOL_VALUE);

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(jsonNode.get(BOOL_KEY).asBoolean(), UPDATED_BOOL_VALUE);
        assertEquals(jsonNode.get(DOUBLE_KEY).asDouble(), DOUBLE_VALUE);
        assertEquals(jsonNode.get(INT_KEY).asInt(), INT_VALUE);
        assertEquals(jsonNode.get(LONG_KEY).asLong(), LONG_VALUE);
        assertEquals(jsonNode.get(STRING_KEY).asText(), STRING_VALUE);

        final List<String> actualComplexValue = OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class);
        assertEquals(actualComplexValue.size(), COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateDouble() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setDouble(DOUBLE_KEY, UPDATED_DOUBLE_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getDoubleRequired(DOUBLE_KEY), UPDATED_DOUBLE_VALUE);

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(jsonNode.get(BOOL_KEY).asBoolean(), BOOL_VALUE);
        assertEquals(jsonNode.get(DOUBLE_KEY).asDouble(), UPDATED_DOUBLE_VALUE);
        assertEquals(jsonNode.get(INT_KEY).asInt(), INT_VALUE);
        assertEquals(jsonNode.get(LONG_KEY).asLong(), LONG_VALUE);
        assertEquals(jsonNode.get(STRING_KEY).asText(), STRING_VALUE);

        final List<String> actualComplexValue = OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class);
        assertEquals(actualComplexValue.size(), COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateInt() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setInt(INT_KEY, UPDATED_INT_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getIntRequired(INT_KEY), UPDATED_INT_VALUE);

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(jsonNode.get(BOOL_KEY).asBoolean(), BOOL_VALUE);
        assertEquals(jsonNode.get(DOUBLE_KEY).asDouble(), DOUBLE_VALUE);
        assertEquals(jsonNode.get(INT_KEY).asInt(), UPDATED_INT_VALUE);
        assertEquals(jsonNode.get(LONG_KEY).asLong(), LONG_VALUE);
        assertEquals(jsonNode.get(STRING_KEY).asText(), STRING_VALUE);

        final List<String> actualComplexValue = OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class);
        assertEquals(actualComplexValue.size(), COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateLong() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setLong(LONG_KEY, UPDATED_LONG_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getLongRequired(LONG_KEY), UPDATED_LONG_VALUE);

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(jsonNode.get(BOOL_KEY).asBoolean(), BOOL_VALUE);
        assertEquals(jsonNode.get(DOUBLE_KEY).asDouble(), DOUBLE_VALUE);
        assertEquals(jsonNode.get(INT_KEY).asInt(), INT_VALUE);
        assertEquals(jsonNode.get(LONG_KEY).asLong(), UPDATED_LONG_VALUE);
        assertEquals(jsonNode.get(STRING_KEY).asText(), STRING_VALUE);

        final List<String> actualComplexValue = OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class);
        assertEquals(actualComplexValue.size(), COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateString() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setString(STRING_KEY, UPDATED_STRING_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getStringRequired(STRING_KEY), UPDATED_STRING_VALUE);

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(jsonNode.get(BOOL_KEY).asBoolean(), BOOL_VALUE);
        assertEquals(jsonNode.get(DOUBLE_KEY).asDouble(), DOUBLE_VALUE);
        assertEquals(jsonNode.get(INT_KEY).asInt(), INT_VALUE);
        assertEquals(jsonNode.get(LONG_KEY).asLong(), LONG_VALUE);
        assertEquals(jsonNode.get(STRING_KEY).asText(), UPDATED_STRING_VALUE);

        final List<String> actualComplexValue = OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class);
        assertEquals(actualComplexValue.size(), COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateObject() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.setObject(COMPLEX_KEY, UPDATED_COMPLEX_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getObjectRequired(COMPLEX_KEY, List.class), UPDATED_COMPLEX_VALUE);

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(jsonNode.get(BOOL_KEY).asBoolean(), BOOL_VALUE);
        assertEquals(jsonNode.get(DOUBLE_KEY).asDouble(), DOUBLE_VALUE);
        assertEquals(jsonNode.get(INT_KEY).asInt(), INT_VALUE);
        assertEquals(jsonNode.get(LONG_KEY).asLong(), LONG_VALUE);
        assertEquals(jsonNode.get(STRING_KEY).asText(), STRING_VALUE);

        final List<String> actualComplexValue = OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class);
        assertEquals(actualComplexValue.size(), UPDATED_COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(UPDATED_COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveProperty() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos);

        // Do the thing
        configSpace.remove(BOOL_KEY);

        // Verify the cache properties were updated
        assertFalse(configSpace.hasValue(BOOL_KEY));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertFalse(jsonNode.has(BOOL_KEY));
        assertEquals(jsonNode.get(DOUBLE_KEY).asDouble(), DOUBLE_VALUE);
        assertEquals(jsonNode.get(INT_KEY).asInt(), INT_VALUE);
        assertEquals(jsonNode.get(LONG_KEY).asLong(), LONG_VALUE);
        assertEquals(jsonNode.get(STRING_KEY).asText(), STRING_VALUE);

        final List<String> actualComplexValue = OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class);
        assertEquals(actualComplexValue.size(), COMPLEX_VALUE.size());
        assertTrue(actualComplexValue.containsAll(COMPLEX_VALUE));
    }
}
