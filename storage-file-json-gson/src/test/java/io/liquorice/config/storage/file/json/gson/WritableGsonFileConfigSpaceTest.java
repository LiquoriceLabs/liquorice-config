package io.liquorice.config.storage.file.json.gson;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.formatter.json.gson.GsonConfigFormatter;

/**
 * Created by mthorpe on 12/13/16.
 */
@ExtendWith(MockitoExtension.class)
class WritableGsonFileConfigSpaceTest {

    private static WritableGsonFileConfigSpace createConfigSpace(final OutputStream outputStream,
            final FileChannel fileChannel) {
        // Initialize seed properties
        final InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(
                JSON_STRING.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        final OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        final Function<FileChannel, Reader> fileChannelReaderFunction = internalFileChannel -> isr;
        final Function<FileChannel, Writer> fileChannelWriterFunction = internalFileChannel -> osw;

        // Initialize sut
        final ConfigFormatter configFormatter = new GsonConfigFormatter.Builder().build();
        return new WritableGsonFileConfigSpace.Builder() //
                .withConfigFormatter(configFormatter) //
                .withFileChannel(fileChannel) //
                .withFileChannelReaderFunction(fileChannelReaderFunction) //
                .withFileChannelWriterFunction(fileChannelWriterFunction) //
                .withGsonBuilder(GSON_BUILDER) //
                .build();
    }

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    @Mock
    private FileChannel mockFileChannel;

    private Gson gson;

    @BeforeEach
    void setup() {
        gson = GSON_BUILDER.create();
    }

    @Test
    void testUpdateBoolean() {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableGsonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setBoolean(BOOL_KEY, UPDATED_BOOL_VALUE);

        // Verify the cache properties were updated
        assertEquals(UPDATED_BOOL_VALUE, configSpace.getBooleanRequired(BOOL_KEY));

        // Verify the on disk properties were updated
        final Reader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        final JsonObject jsonObject = new JsonParser().parse(gson.newJsonReader(isr)).getAsJsonObject();
        assertEquals(UPDATED_BOOL_VALUE, jsonObject.get(BOOL_KEY).getAsBoolean());
        assertEquals(DOUBLE_VALUE, jsonObject.get(DOUBLE_KEY).getAsDouble());
        assertEquals(INT_VALUE, jsonObject.get(INT_KEY).getAsInt());
        assertEquals(LONG_VALUE, jsonObject.get(LONG_KEY).getAsLong());
        assertEquals(STRING_VALUE, jsonObject.get(STRING_KEY).getAsString());
        assertEquals(COMPLEX_VALUE, gson.fromJson(jsonObject.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateDouble() {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableGsonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setDouble(DOUBLE_KEY, UPDATED_DOUBLE_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getDoubleRequired(DOUBLE_KEY), UPDATED_DOUBLE_VALUE);

        // Verify the on disk properties were updated
        final Reader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        final JsonObject jsonObject = new JsonParser().parse(gson.newJsonReader(isr)).getAsJsonObject();
        assertEquals(BOOL_VALUE, jsonObject.get(BOOL_KEY).getAsBoolean());
        assertEquals(UPDATED_DOUBLE_VALUE, jsonObject.get(DOUBLE_KEY).getAsDouble());
        assertEquals(INT_VALUE, jsonObject.get(INT_KEY).getAsInt());
        assertEquals(LONG_VALUE, jsonObject.get(LONG_KEY).getAsLong());
        assertEquals(STRING_VALUE, jsonObject.get(STRING_KEY).getAsString());
        assertEquals(COMPLEX_VALUE, gson.fromJson(jsonObject.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateInt() {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableGsonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setInt(INT_KEY, UPDATED_INT_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getIntRequired(INT_KEY), UPDATED_INT_VALUE);

        // Verify the on disk properties were updated
        final Reader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        final JsonObject jsonObject = new JsonParser().parse(gson.newJsonReader(isr)).getAsJsonObject();
        assertEquals(BOOL_VALUE, jsonObject.get(BOOL_KEY).getAsBoolean());
        assertEquals(DOUBLE_VALUE, jsonObject.get(DOUBLE_KEY).getAsDouble());
        assertEquals(UPDATED_INT_VALUE, jsonObject.get(INT_KEY).getAsInt());
        assertEquals(LONG_VALUE, jsonObject.get(LONG_KEY).getAsLong());
        assertEquals(STRING_VALUE, jsonObject.get(STRING_KEY).getAsString());
        assertEquals(COMPLEX_VALUE, gson.fromJson(jsonObject.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateLong() {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableGsonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setLong(LONG_KEY, UPDATED_LONG_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getLongRequired(LONG_KEY), UPDATED_LONG_VALUE);

        // Verify the on disk properties were updated
        final Reader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        final JsonObject jsonObject = new JsonParser().parse(gson.newJsonReader(isr)).getAsJsonObject();
        assertEquals(BOOL_VALUE, jsonObject.get(BOOL_KEY).getAsBoolean());
        assertEquals(DOUBLE_VALUE, jsonObject.get(DOUBLE_KEY).getAsDouble());
        assertEquals(INT_VALUE, jsonObject.get(INT_KEY).getAsInt());
        assertEquals(UPDATED_LONG_VALUE, jsonObject.get(LONG_KEY).getAsLong());
        assertEquals(STRING_VALUE, jsonObject.get(STRING_KEY).getAsString());
        assertEquals(COMPLEX_VALUE, gson.fromJson(jsonObject.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateString() {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableGsonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setString(STRING_KEY, UPDATED_STRING_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getStringRequired(STRING_KEY), UPDATED_STRING_VALUE);

        // Verify the on disk properties were updated
        final Reader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        final JsonObject jsonObject = new JsonParser().parse(gson.newJsonReader(isr)).getAsJsonObject();
        assertEquals(BOOL_VALUE, jsonObject.get(BOOL_KEY).getAsBoolean());
        assertEquals(DOUBLE_VALUE, jsonObject.get(DOUBLE_KEY).getAsDouble());
        assertEquals(INT_VALUE, jsonObject.get(INT_KEY).getAsInt());
        assertEquals(LONG_VALUE, jsonObject.get(LONG_KEY).getAsLong());
        assertEquals(UPDATED_STRING_VALUE, jsonObject.get(STRING_KEY).getAsString());
        assertEquals(COMPLEX_VALUE, gson.fromJson(jsonObject.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testUpdateObject() {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableGsonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.setObject(COMPLEX_KEY, UPDATED_COMPLEX_VALUE);

        // Verify the cache properties were updated
        assertEquals(configSpace.getObjectRequired(COMPLEX_KEY, List.class), UPDATED_COMPLEX_VALUE);

        // Verify the on disk properties were updated
        final Reader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        final JsonObject jsonObject = new JsonParser().parse(gson.newJsonReader(isr)).getAsJsonObject();
        assertEquals(BOOL_VALUE, jsonObject.get(BOOL_KEY).getAsBoolean());
        assertEquals(DOUBLE_VALUE, jsonObject.get(DOUBLE_KEY).getAsDouble());
        assertEquals(INT_VALUE, jsonObject.get(INT_KEY).getAsInt());
        assertEquals(LONG_VALUE, jsonObject.get(LONG_KEY).getAsLong());
        assertEquals(STRING_VALUE, jsonObject.get(STRING_KEY).getAsString());
        assertEquals(UPDATED_COMPLEX_VALUE, gson.fromJson(jsonObject.get(COMPLEX_KEY), List.class));
    }

    @Test
    void testRemoveProperty() {
        // Setup mocks
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableGsonFileConfigSpace configSpace = createConfigSpace(baos, mockFileChannel);

        // Do the thing
        configSpace.remove(BOOL_KEY);

        // Verify the cache properties were updated
        assertFalse(configSpace.hasValue(BOOL_KEY));

        // Verify the on disk properties were updated
        final Reader isr = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8);
        final JsonObject jsonObject = new JsonParser().parse(gson.newJsonReader(isr)).getAsJsonObject();
        assertFalse(jsonObject.has(BOOL_KEY));
        assertEquals(DOUBLE_VALUE, jsonObject.get(DOUBLE_KEY).getAsDouble());
        assertEquals(INT_VALUE, jsonObject.get(INT_KEY).getAsInt());
        assertEquals(LONG_VALUE, jsonObject.get(LONG_KEY).getAsLong());
        assertEquals(STRING_VALUE, jsonObject.get(STRING_KEY).getAsString());
        assertEquals(COMPLEX_VALUE, gson.fromJson(jsonObject.get(COMPLEX_KEY), List.class));
    }
}
