package io.liquorice.config.storage.memory;

import com.google.common.collect.ImmutableMap;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.liquorice.config.storage.memory.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.BOOL_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.COMPLEX_KEY;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.COMPLEX_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DEFAULT_BOOL_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DEFAULT_COMPLEX_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DEFAULT_DOUBLE_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DEFAULT_INT_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DEFAULT_LONG_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DEFAULT_STRING_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DOES_NOT_EXIST_KEY;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DOUBLE_KEY;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.DOUBLE_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.INT_KEY;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.INT_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.LONG_KEY;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.LONG_VALUE;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.STRING_KEY;
import static io.liquorice.config.storage.memory.ConfigSpaceTestData.STRING_VALUE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by mthorpe on 12/11/16.
 */
public class ReadableMapConfigSpaceTest {

    private ReadableMapConfigSpace configSpace;

    @BeforeTest
    public void setup() {
        // Initialize seed properties
        final Map<String, Object> seedProperties = new ImmutableMap.Builder<String, Object>() //
                .put(BOOL_KEY, BOOL_VALUE) //
                .put(DOUBLE_KEY, DOUBLE_VALUE) //
                .put(INT_KEY, INT_VALUE) //
                .put(LONG_KEY, LONG_VALUE) //
                .put(STRING_KEY, STRING_VALUE) //
                .put(COMPLEX_KEY, COMPLEX_VALUE) //
                .build();

        final ConfigFormatter configFormatter = new PassThroughConfigFormatter();
        configSpace = new ReadableMapConfigSpace(configFormatter, seedProperties);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGettersReturnCorrectValues() {
        assertEquals(configSpace.getBoolean(BOOL_KEY, DEFAULT_BOOL_VALUE), BOOL_VALUE);
        assertEquals(configSpace.getDouble(DOUBLE_KEY, DEFAULT_DOUBLE_VALUE), DOUBLE_VALUE);
        assertEquals(configSpace.getInt(INT_KEY, DEFAULT_INT_VALUE), INT_VALUE);
        assertEquals(configSpace.getLong(LONG_KEY, DEFAULT_LONG_VALUE), LONG_VALUE);
        assertEquals(configSpace.getString(STRING_KEY, DEFAULT_STRING_VALUE), STRING_VALUE);

        final List<String> configObject = (List<String>) configSpace.getObject(COMPLEX_KEY, DEFAULT_COMPLEX_VALUE,
                List.class);
        assertEquals(configObject.size(), COMPLEX_VALUE.size());
        assertTrue(configObject.containsAll(COMPLEX_VALUE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGettersReturnDefaultValues() {
        assertEquals(configSpace.getBoolean(DOES_NOT_EXIST_KEY, DEFAULT_BOOL_VALUE), DEFAULT_BOOL_VALUE);
        assertEquals(configSpace.getDouble(DOES_NOT_EXIST_KEY, DEFAULT_DOUBLE_VALUE), DEFAULT_DOUBLE_VALUE);
        assertEquals(configSpace.getInt(DOES_NOT_EXIST_KEY, DEFAULT_INT_VALUE), DEFAULT_INT_VALUE);
        assertEquals(configSpace.getLong(DOES_NOT_EXIST_KEY, DEFAULT_LONG_VALUE), DEFAULT_LONG_VALUE);
        assertEquals(configSpace.getString(DOES_NOT_EXIST_KEY, DEFAULT_STRING_VALUE), DEFAULT_STRING_VALUE);

        final List<String> configObject = (List<String>) configSpace.getObject(DOES_NOT_EXIST_KEY,
                DEFAULT_COMPLEX_VALUE, List.class);
        assertEquals(configObject.size(), DEFAULT_COMPLEX_VALUE.size());
        assertTrue(configObject.containsAll(DEFAULT_COMPLEX_VALUE));
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

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredObjectPropertyDoesNotExist() {
        configSpace.getObjectRequired(DOES_NOT_EXIST_KEY, List.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPropertyOfWrongTypeReturnsDefaultValue() {
        assertEquals(configSpace.getBoolean(DOUBLE_KEY, DEFAULT_BOOL_VALUE), DEFAULT_BOOL_VALUE);
        assertEquals(configSpace.getDouble(INT_KEY, DEFAULT_DOUBLE_VALUE), DEFAULT_DOUBLE_VALUE);
        assertEquals(configSpace.getInt(LONG_KEY, DEFAULT_INT_VALUE), DEFAULT_INT_VALUE);
        assertEquals(configSpace.getLong(STRING_KEY, DEFAULT_LONG_VALUE), DEFAULT_LONG_VALUE);
        assertEquals(configSpace.getString(COMPLEX_KEY, DEFAULT_STRING_VALUE), DEFAULT_STRING_VALUE);

        final List<String> configObject = (List<String>) configSpace.getObject(BOOL_KEY, DEFAULT_COMPLEX_VALUE,
                List.class);
        assertEquals(configObject.size(), DEFAULT_COMPLEX_VALUE.size());
        assertTrue(configObject.containsAll(DEFAULT_COMPLEX_VALUE));
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredBooleanPropertyOfWrongType() {
        configSpace.getBooleanRequired(DOUBLE_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredDoublePropertyOfWrongType() {
        configSpace.getDoubleRequired(INT_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredIntegerPropertyOfWrongType() {
        configSpace.getIntRequired(LONG_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredLongPropertyOfWrongType() {
        configSpace.getLongRequired(STRING_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredStringPropertyOfWrongType() {
        configSpace.getStringRequired(COMPLEX_KEY);
    }

    @Test(expectedExceptions = ConfigurationException.class)
    public void testRequiredObjectPropertyOfWrongType() {
        configSpace.getObjectRequired(BOOL_KEY, List.class);
    }
}
