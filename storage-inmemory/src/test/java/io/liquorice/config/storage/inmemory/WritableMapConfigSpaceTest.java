package io.liquorice.config.storage.inmemory;

import static io.liquorice.config.storage.inmemory.Fixtures.SEED_PROPERTIES;
import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.COMPLEX_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.DOUBLE_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.INT_VALUE;
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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;

/**
 * Created by mthorpe on 12/13/16.
 */
class WritableMapConfigSpaceTest {

    private WritableMapConfigSpace configSpace;

    @BeforeEach
    void setup() {
        final ConfigFormatter configFormatter = new PassThroughConfigFormatter();
        configSpace = new WritableMapConfigSpace(configFormatter, SEED_PROPERTIES);
    }

    @Test
    void testSetters() {
        // Boolean
        configSpace.setBoolean(BOOL_KEY, UPDATED_BOOL_VALUE);
        assertEquals(UPDATED_BOOL_VALUE, configSpace.getBooleanRequired(BOOL_KEY));

        // Double
        configSpace.setDouble(DOUBLE_KEY, UPDATED_DOUBLE_VALUE);
        assertEquals(UPDATED_DOUBLE_VALUE, configSpace.getDoubleRequired(DOUBLE_KEY));

        // Integer
        configSpace.setInt(INT_KEY, UPDATED_INT_VALUE);
        assertEquals(UPDATED_INT_VALUE, configSpace.getIntRequired(INT_KEY));

        // Long
        configSpace.setLong(LONG_KEY, UPDATED_LONG_VALUE);
        assertEquals(UPDATED_LONG_VALUE, configSpace.getLongRequired(LONG_KEY));

        // String
        configSpace.setString(STRING_KEY, UPDATED_STRING_VALUE);
        assertEquals(UPDATED_STRING_VALUE, configSpace.getStringRequired(STRING_KEY));

        // Complex Objects
        configSpace.setObject(COMPLEX_KEY, UPDATED_COMPLEX_VALUE);
        assertEquals(UPDATED_COMPLEX_VALUE, configSpace.getObjectRequired(COMPLEX_KEY, List.class));
    }

    @Test
    void testRemoveProperty() {
        configSpace.remove(BOOL_KEY);

        assertFalse(configSpace.hasValue(BOOL_KEY));
        assertEquals(DOUBLE_VALUE, configSpace.getDoubleRequired(DOUBLE_KEY));
        assertEquals(INT_VALUE, configSpace.getIntRequired(INT_KEY));
        assertEquals(LONG_VALUE, configSpace.getLongRequired(LONG_KEY));
        assertEquals(STRING_VALUE, configSpace.getStringRequired(STRING_KEY));
    }

}
