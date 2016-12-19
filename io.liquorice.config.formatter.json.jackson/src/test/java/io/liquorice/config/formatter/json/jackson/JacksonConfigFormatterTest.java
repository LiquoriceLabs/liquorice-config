package io.liquorice.config.formatter.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Charsets;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.NoSuchElementException;

import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_JSON;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_MAP;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_NOT_JSON;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_VALUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by mthorpe on 12/11/16.
 */
public class JacksonConfigFormatterTest {

    private JacksonConfigFormatter configFormatter;

    @BeforeTest
    public void setup() {
        this.configFormatter = new JacksonConfigFormatter.Builder().build();
    }

    @Test
    public void testModuleRegistration() {
        final SimpleModule simpleModule = new SimpleModule();
        final ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        final JacksonConfigFormatter jacksonConfigFormatter = new JacksonConfigFormatter.Builder() //
                .withObjectMapper(mockObjectMapper) //
                .withRegisteredModule(simpleModule) //
                .build();
        assertNotNull(jacksonConfigFormatter);

        verify(mockObjectMapper).registerModule(simpleModule);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadObjectIsInputStream() throws Exception {
        final Object downcastedInputStream = new ByteArrayInputStream(TEST_JSON.getBytes(Charsets.UTF_8));
        final Map<String, String> read = (Map<String, String>) configFormatter.read(downcastedInputStream, Map.class)
                .get();
        assertEquals(read, TEST_MAP);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testReadObjectIsMalformedInputStreamContent() throws Exception {
        final Object downcastedInputStream = new ByteArrayInputStream(TEST_NOT_JSON.getBytes(Charsets.UTF_8));
        configFormatter.read(downcastedInputStream, Map.class).get();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadObjectIsReader() throws Exception {
        final Object downcastedInputStream = new InputStreamReader(new ByteArrayInputStream(
                TEST_JSON.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        final Map<String, String> read = (Map<String, String>) configFormatter.read(downcastedInputStream, Map.class)
                .get();
        assertEquals(read, TEST_MAP);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testReadObjectIsMalformedReaderContent() throws Exception {
        final Object downcastedInputStream = new InputStreamReader(new ByteArrayInputStream(
                TEST_NOT_JSON.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        configFormatter.read(downcastedInputStream, Map.class).get();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadObjectIsString() throws Exception {
        final Object downcastedString = TEST_JSON;
        final Map<String, String> read = (Map<String, String>) configFormatter.read(downcastedString, Map.class).get();
        assertEquals(read, TEST_MAP);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testReadObjectIsMalformedStringContent() throws Exception {
        final Object downcastedString = TEST_NOT_JSON;
        configFormatter.read(downcastedString, Map.class).get();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadObjectIsJsonNode() throws Exception {
        final Object jsonNode = new TextNode(TEST_VALUE);
        final String read = configFormatter.read(jsonNode, String.class).get();
        assertEquals(read, TEST_VALUE);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testReadObjectIsMalformedJsonNode() throws Exception {
        final JsonProcessingException mockException = mock(JsonProcessingException.class);
        final ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        final JsonNode jsonNode = new TextNode(TEST_VALUE);
        when(mockObjectMapper.treeToValue(eq(jsonNode), any())).thenThrow(mockException);

        final JacksonConfigFormatter exceptionInducingObjectMapper = new JacksonConfigFormatter.Builder() //
                .withObjectMapper(mockObjectMapper) //
                .build();
        exceptionInducingObjectMapper.read(jsonNode, String.class).get();
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = NoSuchElementException.class)
    public void testUnsupportedInputType() throws Exception {
        final Object downcastedDouble = 2.0;
        configFormatter.read(downcastedDouble, Map.class).get();
    }

    @Test
    public void testWrite() throws Exception {
        final Object written = configFormatter.write(TEST_MAP);
        assertTrue(written instanceof String);

        final String writtenJson = (String) written;
        assertEquals(writtenJson, TEST_JSON);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testWriteException() throws Exception {
        final JsonProcessingException mockException = mock(JsonProcessingException.class);
        final ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        when(mockObjectMapper.writeValueAsString(eq(TEST_VALUE))).thenThrow(mockException);

        final JacksonConfigFormatter exceptionInducingObjectMapper = new JacksonConfigFormatter.Builder() //
                .withObjectMapper(mockObjectMapper) //
                .build();
        exceptionInducingObjectMapper.write(TEST_VALUE);
    }
}
