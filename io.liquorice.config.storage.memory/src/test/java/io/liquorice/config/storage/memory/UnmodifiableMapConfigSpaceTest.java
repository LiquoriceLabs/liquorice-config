package io.liquorice.config.storage.memory;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableMap;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.exception.ConfigurationException;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by mthorpe on 12/11/16.
 */
public class UnmodifiableMapConfigSpaceTest {

    private static final String BOOL_KEY = "key.bool";
    private static final String DOUBLE_KEY = "key.double";
    private static final String INT_KEY = "key.int";
    private static final String LONG_KEY = "key.long";
    private static final String STRING_KEY = "key.string";
    private static final String COMPLEX_KEY = "key.complex";
    private static final String DOES_NOT_EXIST_KEY = "key.does.not.exist";

    private static final boolean BOOL_VALUE = true;
    private static final double DOUBLE_VALUE = 1.6;
    private static final int INT_VALUE = 7;
    private static final long LONG_VALUE = 17L;
    private static final String STRING_VALUE = "test string";
    private static final List<String> COMPLEX_VALUE = Lists.newArrayList(BOOL_KEY, DOUBLE_KEY);

    private static final boolean DEFAULT_BOOL_VALUE = false;
    private static final double DEFAULT_DOUBLE_VALUE = 2.4;
    private static final int DEFAULT_INT_VALUE = 18;
    private static final long DEFAULT_LONG_VALUE = 24L;
    private static final String DEFAULT_STRING_VALUE = "default test value";
    private static final List<String> DEFAULT_COMPLEX_VALUE = Lists.newArrayList(INT_KEY, LONG_KEY);

    private UnmodifiableMapConfigSpace configSpace;

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

        final ConfigFormatter mockConfigFormatter = new PassThroughConfigFormatter();
        configSpace = new UnmodifiableMapConfigSpace(mockConfigFormatter, seedProperties);
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
