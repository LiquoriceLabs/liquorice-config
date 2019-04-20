package io.liquorice.config.formatter.json.jackson;

import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_JSON;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_MAP;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_NOT_JSON;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Charsets;

/**
 * Created by mthorpe on 12/11/16.
 */
class JacksonConfigFormatterTest {

    private JacksonConfigFormatter configFormatter;

    @BeforeEach
    void setup() {
        this.configFormatter = new JacksonConfigFormatter.Builder().build();
    }

    @Test
    void testModuleRegistration() {
        final SimpleModule simpleModule = new SimpleModule();
        final ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        final JacksonConfigFormatter jacksonConfigFormatter = new JacksonConfigFormatter.Builder() //
                .withObjectMapper(mockObjectMapper) //
                .withRegisteredModule(simpleModule) //
                .build();
        assertNotNull(jacksonConfigFormatter);

        verify(mockObjectMapper).registerModule(simpleModule);
    }

    @Test
    void testReadObjectIsInputStream() throws Exception {
        final Object downcastedInputStream = new ByteArrayInputStream(TEST_JSON.getBytes(Charsets.UTF_8));
        assertEquals(TEST_MAP, configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsMalformedInputStreamContent() throws Exception {
        final Object downcastedInputStream = new ByteArrayInputStream(TEST_NOT_JSON.getBytes(Charsets.UTF_8));
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsReader() throws Exception {
        final Object downcastedInputStream = new InputStreamReader(new ByteArrayInputStream(
                TEST_JSON.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        assertEquals(TEST_MAP, configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsMalformedReaderContent() throws Exception {
        final Object downcastedInputStream = new InputStreamReader(new ByteArrayInputStream(
                TEST_NOT_JSON.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(downcastedInputStream, Map.class).get());
    }

    @Test
    void testReadObjectIsString() throws Exception {
        assertEquals(TEST_MAP, configFormatter.read(TEST_JSON, Map.class).get());
    }

    @Test
    void testReadObjectIsMalformedStringContent() throws Exception {
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(TEST_NOT_JSON, Map.class).get());
    }

    @Test
    void testReadObjectIsJsonNode() throws Exception {
        final Object jsonNode = new TextNode(TEST_VALUE);
        assertEquals(TEST_VALUE, configFormatter.read(jsonNode, String.class).get());
    }

    @Test
    void testReadObjectIsMalformedJsonNode() throws Exception {
        final JsonProcessingException mockException = mock(JsonProcessingException.class);
        final ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        final JsonNode jsonNode = new TextNode(TEST_VALUE);
        when(mockObjectMapper.treeToValue(eq(jsonNode), any())).thenThrow(mockException);

        final JacksonConfigFormatter exceptionInducingObjectMapper = new JacksonConfigFormatter.Builder() //
                .withObjectMapper(mockObjectMapper) //
                .build();
        assertThrows(NoSuchElementException.class, () -> exceptionInducingObjectMapper.read(jsonNode, String.class).get());
    }

    @Test
    void testUnsupportedInputType() throws Exception {
        final Object downcastedDouble = 2.0;
        assertThrows(NoSuchElementException.class, () -> configFormatter.read(downcastedDouble, Map.class).get());
    }

    @Test
    void testWrite() throws Exception {
        final Object written = configFormatter.write(TEST_MAP);
        assertTrue(written instanceof String);
        assertEquals(TEST_JSON, written);
    }

    @Test
    void testWriteException() throws Exception {
        final JsonProcessingException mockException = mock(JsonProcessingException.class);
        final ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        when(mockObjectMapper.writeValueAsString(eq(TEST_VALUE))).thenThrow(mockException);

        final JacksonConfigFormatter exceptionInducingObjectMapper = new JacksonConfigFormatter.Builder() //
                .withObjectMapper(mockObjectMapper) //
                .build();
        assertThrows(RuntimeException.class, () -> exceptionInducingObjectMapper.write(TEST_VALUE));
    }
}
