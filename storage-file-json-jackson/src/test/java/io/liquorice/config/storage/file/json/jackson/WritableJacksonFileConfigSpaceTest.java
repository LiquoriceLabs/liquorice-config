package io.liquorice.config.storage.file.json.jackson;

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
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.formatter.json.jackson.JacksonConfigFormatter;

/**
 * Created by mthorpe on 12/13/16.
 */
@ExtendWith(MockitoExtension.class)
class WritableJacksonFileConfigSpaceTest {

    private static WritableJacksonFileConfigSpace createConfigSpace(final OutputStream outputStream,
            final FileChannel fileChannel) {
        // Initialize seed properties
        final InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(
                JSON_STRING.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        final OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        final Function<FileChannel, Reader> fileChannelReaderFunction = internalFileChannel -> isr;
        final Function<FileChannel, Writer> fileChannelWriterFunction = internalFileChannel -> osw;

        // Initialize sut
        final ConfigFormatter configFormatter = new JacksonConfigFormatter.Builder().build();
        return new WritableJacksonFileConfigSpace.Builder() //
                .withConfigFormatter(configFormatter) //
                .withFileChannel(fileChannel) //
                .withFileChannelReaderFunction(fileChannelReaderFunction) //
                .withFileChannelWriterFunction(fileChannelWriterFunction) //
                .withObjectMapper(OBJECT_MAPPER) //
                .build();
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private FileChannel mockFileChannel;

    @Test
    void testUpdateBoolean() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setBoolean(BOOL_KEY, UPDATED_BOOL_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_BOOL_VALUE, configSpace.getBooleanRequired(BOOL_KEY));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(UPDATED_BOOL_VALUE, jsonNode.get(BOOL_KEY).asBoolean());
        assertEquals(DOUBLE_VALUE, jsonNode.get(DOUBLE_KEY).asDouble());
        assertEquals(INT_VALUE, jsonNode.get(INT_KEY).asInt());
        assertEquals(LONG_VALUE, jsonNode.get(LONG_KEY).asLong());
        assertEquals(STRING_VALUE, jsonNode.get(STRING_KEY).asText());
        assertEquals(COMPLEX_VALUE, OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateDouble() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setDouble(DOUBLE_KEY, UPDATED_DOUBLE_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_DOUBLE_VALUE, configSpace.getDoubleRequired(DOUBLE_KEY));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, jsonNode.get(BOOL_KEY).asBoolean());
        assertEquals(UPDATED_DOUBLE_VALUE, jsonNode.get(DOUBLE_KEY).asDouble());
        assertEquals(INT_VALUE, jsonNode.get(INT_KEY).asInt());
        assertEquals(LONG_VALUE, jsonNode.get(LONG_KEY).asLong());
        assertEquals(STRING_VALUE, jsonNode.get(STRING_KEY).asText());
        assertEquals(COMPLEX_VALUE, OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateInt() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setInt(INT_KEY, UPDATED_INT_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_INT_VALUE, configSpace.getIntRequired(INT_KEY));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, jsonNode.get(BOOL_KEY).asBoolean());
        assertEquals(DOUBLE_VALUE, jsonNode.get(DOUBLE_KEY).asDouble());
        assertEquals(UPDATED_INT_VALUE, jsonNode.get(INT_KEY).asInt());
        assertEquals(LONG_VALUE, jsonNode.get(LONG_KEY).asLong());
        assertEquals(STRING_VALUE, jsonNode.get(STRING_KEY).asText());
        assertEquals(COMPLEX_VALUE, OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateLong() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setLong(LONG_KEY, UPDATED_LONG_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_LONG_VALUE, configSpace.getLongRequired(LONG_KEY));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, jsonNode.get(BOOL_KEY).asBoolean());
        assertEquals(DOUBLE_VALUE, jsonNode.get(DOUBLE_KEY).asDouble());
        assertEquals(INT_VALUE, jsonNode.get(INT_KEY).asInt());
        assertEquals(UPDATED_LONG_VALUE, jsonNode.get(LONG_KEY).asLong());
        assertEquals(STRING_VALUE, jsonNode.get(STRING_KEY).asText());
        assertEquals(COMPLEX_VALUE, OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateString() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setString(STRING_KEY, UPDATED_STRING_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_STRING_VALUE, configSpace.getStringRequired(STRING_KEY));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, jsonNode.get(BOOL_KEY).asBoolean());
        assertEquals(DOUBLE_VALUE, jsonNode.get(DOUBLE_KEY).asDouble());
        assertEquals(INT_VALUE, jsonNode.get(INT_KEY).asInt());
        assertEquals(LONG_VALUE, jsonNode.get(LONG_KEY).asLong());
        assertEquals(UPDATED_STRING_VALUE, jsonNode.get(STRING_KEY).asText());
        assertEquals(COMPLEX_VALUE, OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateObject() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setObject(COMPLEX_KEY, UPDATED_COMPLEX_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_COMPLEX_VALUE, configSpace.getObjectRequired(COMPLEX_KEY, List.class));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(BOOL_VALUE, jsonNode.get(BOOL_KEY).asBoolean());
        assertEquals(DOUBLE_VALUE, jsonNode.get(DOUBLE_KEY).asDouble());
        assertEquals(INT_VALUE, jsonNode.get(INT_KEY).asInt());
        assertEquals(LONG_VALUE, jsonNode.get(LONG_KEY).asLong());
        assertEquals(STRING_VALUE, jsonNode.get(STRING_KEY).asText());
        assertEquals(UPDATED_COMPLEX_VALUE, OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testRemoveProperty() throws Exception {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableJacksonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.remove(BOOL_KEY);

        // Verify the cache properties were updated
        assertFalse(configSpace.hasValue(BOOL_KEY));

        // Verify the on disk properties were updated
        final JsonNode jsonNode = OBJECT_MAPPER.readTree(new ByteArrayInputStream(baos.toByteArray()));
        assertFalse(jsonNode.has(BOOL_KEY));
        assertEquals(DOUBLE_VALUE, jsonNode.get(DOUBLE_KEY).asDouble());
        assertEquals(INT_VALUE, jsonNode.get(INT_KEY).asInt());
        assertEquals(LONG_VALUE, jsonNode.get(LONG_KEY).asLong());
        assertEquals(STRING_VALUE, jsonNode.get(STRING_KEY).asText());
        assertEquals(COMPLEX_VALUE, OBJECT_MAPPER.treeToValue(jsonNode.get(COMPLEX_KEY), List.class));
    }
}
