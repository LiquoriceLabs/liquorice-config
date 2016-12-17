package io.liquorice.config.storage.memory;

import com.google.common.collect.ImmutableMap;
import io.liquorice.config.api.formatter.ConfigFormatter;
import io.liquorice.config.formatter.passthrough.PassThroughConfigFormatter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.BOOL_VALUE;
import static io.liquorice.config.test.support.ConfigSpaceTestData.COMPLEX_KEY;
import static io.liquorice.config.test.support.ConfigSpaceTestData.COMPLEX_VALUE;
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by mthorpe on 12/13/16.
 */
public class WritableMapConfigSpaceTest {

    private WritableMapConfigSpace configSpace;

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
        configSpace = new WritableMapConfigSpace(configFormatter, seedProperties);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetters() {
        // Boolean
        configSpace.setBoolean(BOOL_KEY, UPDATED_BOOL_VALUE);
        assertEquals(configSpace.getBooleanRequired(BOOL_KEY), UPDATED_BOOL_VALUE);

        // Double
        configSpace.setDouble(DOUBLE_KEY, UPDATED_DOUBLE_VALUE);
        assertEquals(configSpace.getDoubleRequired(DOUBLE_KEY), UPDATED_DOUBLE_VALUE);

        // Integer
        configSpace.setInt(INT_KEY, UPDATED_INT_VALUE);
        assertEquals(configSpace.getIntRequired(INT_KEY), UPDATED_INT_VALUE);

        // Long
        configSpace.setLong(LONG_KEY, UPDATED_LONG_VALUE);
        assertEquals(configSpace.getLongRequired(LONG_KEY), UPDATED_LONG_VALUE);

        // String
        configSpace.setString(STRING_KEY, UPDATED_STRING_VALUE);
        assertEquals(configSpace.getStringRequired(STRING_KEY), UPDATED_STRING_VALUE);

        // Complex Objects
        configSpace.setObject(COMPLEX_KEY, UPDATED_COMPLEX_VALUE);
        final List<String> actualComplex = (List<String>) configSpace.getObjectRequired(COMPLEX_KEY, List.class);
        assertEquals(actualComplex.size(), UPDATED_COMPLEX_VALUE.size());
        assertTrue(actualComplex.containsAll(UPDATED_COMPLEX_VALUE));
    }
}
