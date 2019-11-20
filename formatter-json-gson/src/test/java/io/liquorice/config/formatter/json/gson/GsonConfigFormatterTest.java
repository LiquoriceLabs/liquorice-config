package io.liquorice.config.formatter.json.gson;

import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_JSON;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_MAP;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_NOT_JSON;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;

/**
 * Created by mthorpe on 12/11/16.
 */
@ExtendWith(MockitoExtension.class)
class GsonConfigFormatterTest {

    @Mock
    private TypeAdapter<String> mockTypeAdapter;

    private GsonConfigFormatter configFormatter;

    @BeforeEach
    void setup() {
        this.configFormatter = new GsonConfigFormatter.Builder().build();
    }

    @Test
    void testTypeAdapterRegistration() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final GsonConfigFormatter gsonConfigFormatter = new GsonConfigFormatter.Builder() //
                .withGsonBuilder(gsonBuilder) //
                .withTypeAdapter(String.class, mockTypeAdapter) //
                .build();
        assertNotNull(gsonConfigFormatter);

        final Gson gson = gsonBuilder.create();
        assertEquals(mockTypeAdapter, gson.getAdapter(String.class));
    }

    @Test
    void testReadObjectIsInputStream() {
        final Object downcastedInputStream = new ByteArrayInputStream(TEST_JSON.getBytes(StandardCharsets.UTF_8));
        assertEquals(TEST_MAP, configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsMalformedInputStreamContent() {
        final Object downcastedInputStream = new ByteArrayInputStream(TEST_NOT_JSON.getBytes(StandardCharsets.UTF_8));
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsReader() {
        final Object downcastedInputStream = new InputStreamReader(new ByteArrayInputStream(
                TEST_JSON.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        assertEquals(TEST_MAP, configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsMalformedReaderContent() {
        final Object downcastedInputStream = new InputStreamReader(new ByteArrayInputStream(
                TEST_NOT_JSON.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsString() {
        assertEquals(TEST_MAP, configFormatter.read(TEST_JSON, Map.class).get());
    }

    @Test
    void testReadObjectIsMalformedStringContent() {
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(TEST_NOT_JSON, Map.class).get());
    }

    @Test
    void testReadObjectIsJsonElement() {
        final Object jsonElement = new JsonPrimitive(TEST_VALUE);
        final String read = configFormatter.read(jsonElement, String.class).get();
        assertEquals(TEST_VALUE, read);
    }

    @Test
    void testUnsupportedInputType() {
        final Object downcastedDouble = 2.0;
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(downcastedDouble, Map.class).get());
    }

    @Test
    void testWrite() {
        final Object written = configFormatter.write(TEST_MAP);
        assertTrue(written instanceof String);
        assertEquals(TEST_JSON, written);
    }
}
