package io.liquorice.config.storage.inmemory;

import static io.liquorice.config.storage.inmemory.Fixtures.SEED_PROPERTIES;
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
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.LONG_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.STRING_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;

/**
 * Created by mthorpe on 12/11/16.
 */
class ReadableMapConfigSpaceTest {

    private ReadableMapConfigSpace configSpace;

    @BeforeEach
    void setup() {
        final ConfigFormatter configFormatter = new PassThroughConfigFormatter();
        configSpace = new ReadableMapConfigSpace(configFormatter, SEED_PROPERTIES);
    }

    @Test
    void testGettersReturnCorrectValues() {
        assertEquals(BOOL_VALUE, configSpace.getBoolean(BOOL_KEY, DEFAULT_BOOL_VALUE));
        assertEquals(DOUBLE_VALUE, configSpace.getDouble(DOUBLE_KEY, DEFAULT_DOUBLE_VALUE));
        assertEquals(INT_VALUE, configSpace.getInt(INT_KEY, DEFAULT_INT_VALUE));
        assertEquals(LONG_VALUE, configSpace.getLong(LONG_KEY, DEFAULT_LONG_VALUE));
        assertEquals(STRING_VALUE, configSpace.getString(STRING_KEY, DEFAULT_STRING_VALUE));

        assertEquals(COMPLEX_VALUE, configSpace.getObject(COMPLEX_KEY, DEFAULT_COMPLEX_VALUE, List.class));
    }

    @Test
    void testGettersReturnDefaultValues() {
        assertEquals(DEFAULT_BOOL_VALUE, configSpace.getBoolean(DOES_NOT_EXIST_KEY, DEFAULT_BOOL_VALUE));
        assertEquals(DEFAULT_DOUBLE_VALUE, configSpace.getDouble(DOES_NOT_EXIST_KEY, DEFAULT_DOUBLE_VALUE));
        assertEquals(DEFAULT_INT_VALUE, configSpace.getInt(DOES_NOT_EXIST_KEY, DEFAULT_INT_VALUE));
        assertEquals(DEFAULT_LONG_VALUE, configSpace.getLong(DOES_NOT_EXIST_KEY, DEFAULT_LONG_VALUE));
        assertEquals(DEFAULT_STRING_VALUE, configSpace.getString(DOES_NOT_EXIST_KEY, DEFAULT_STRING_VALUE));

        assertEquals(DEFAULT_COMPLEX_VALUE, configSpace.getObject(DOES_NOT_EXIST_KEY, DEFAULT_COMPLEX_VALUE, List.class));
    }

    @Test
    void testRequiredBooleanPropertyDoesNotExist() {
        assertThrows(ConfigurationException.class, () -> configSpace.getBooleanRequired(DOES_NOT_EXIST_KEY));
    }

    @Test
    void testRequiredDoublePropertyDoesNotExist() {
        assertThrows(ConfigurationException.class, () -> configSpace.getDoubleRequired(DOES_NOT_EXIST_KEY));
    }

    @Test
    void testRequiredIntegerPropertyDoesNotExist() {
        assertThrows(ConfigurationException.class, () -> configSpace.getIntRequired(DOES_NOT_EXIST_KEY));
    }

    @Test
    void testRequiredLongPropertyDoesNotExist() {
        assertThrows(ConfigurationException.class, () -> configSpace.getLongRequired(DOES_NOT_EXIST_KEY));
    }

    @Test
    void testRequiredStringPropertyDoesNotExist() {
        assertThrows(ConfigurationException.class, () -> configSpace.getStringRequired(DOES_NOT_EXIST_KEY));
    }

    @Test
    void testRequiredObjectPropertyDoesNotExist() {
        assertThrows(ConfigurationException.class, () -> configSpace.getObjectRequired(DOES_NOT_EXIST_KEY, List.class));
    }

    @Test
    void testPropertyOfWrongTypeReturnsDefaultValue() {
        assertEquals(DEFAULT_BOOL_VALUE, configSpace.getBoolean(DOUBLE_KEY, DEFAULT_BOOL_VALUE));
        assertEquals(DEFAULT_DOUBLE_VALUE, configSpace.getDouble(BOOL_KEY, DEFAULT_DOUBLE_VALUE));
        assertEquals(DEFAULT_INT_VALUE, configSpace.getInt(BOOL_KEY, DEFAULT_INT_VALUE));
        assertEquals(DEFAULT_LONG_VALUE, configSpace.getLong(BOOL_KEY, DEFAULT_LONG_VALUE));
        assertEquals(DEFAULT_STRING_VALUE, configSpace.getString(COMPLEX_KEY, DEFAULT_STRING_VALUE));

        assertEquals(DEFAULT_COMPLEX_VALUE, configSpace.getObject(BOOL_KEY, DEFAULT_COMPLEX_VALUE, List.class));
    }

    @Test
    void testRequiredBooleanPropertyOfWrongType() {
        assertThrows(ConfigurationException.class, () -> configSpace.getBooleanRequired(DOUBLE_KEY));
    }

    @Test
    void testRequiredDoublePropertyOfWrongType() {
        assertThrows(ConfigurationException.class, () -> configSpace.getDoubleRequired(INT_KEY));
    }

    @Test
    void testRequiredIntegerPropertyOfWrongType() {
        assertThrows(ConfigurationException.class, () -> configSpace.getIntRequired(LONG_KEY));
    }

    @Test
    void testRequiredLongPropertyOfWrongType() {
        assertThrows(ConfigurationException.class, () -> configSpace.getLongRequired(STRING_KEY));
    }

    @Test
    void testRequiredStringPropertyOfWrongType() {
        assertThrows(ConfigurationException.class, () -> configSpace.getStringRequired(COMPLEX_KEY));
    }

    @Test
    void testRequiredObjectPropertyOfWrongType() {
        assertThrows(ConfigurationException.class, () -> configSpace.getObjectRequired(BOOL_KEY, List.class));
    }
}
