package io.liquorice.config.storage.inmemory;

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

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public final class Fixtures {

    private Fixtures() {

    }

    public static final Map<String, Object> SEED_PROPERTIES = ImmutableMap.<String, Object>builder()
            .put(BOOL_KEY, BOOL_VALUE)
            .put(DOUBLE_KEY, DOUBLE_VALUE)
            .put(INT_KEY, INT_VALUE)
            .put(LONG_KEY, LONG_VALUE)
            .put(STRING_KEY, STRING_VALUE)
            .put(COMPLEX_KEY, COMPLEX_VALUE)
            .build();
}
