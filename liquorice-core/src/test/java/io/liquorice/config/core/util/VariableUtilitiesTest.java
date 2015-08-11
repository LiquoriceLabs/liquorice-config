package io.liquorice.config.core.util;

import io.liquorice.config.core.utils.VariableUtilities;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by mthorpe on 6/10/15.
 */
public class VariableUtilitiesTest {
    private static final String DEFAULT_VALUE = "default";

    @DataProvider(name = "variableDataProvider")
    public static Object[][] variables() {
        return new Object[][] { { "good", "good" }, { "", DEFAULT_VALUE }, { null, DEFAULT_VALUE } };
    }

    @Test(dataProvider = "variableDataProvider")
    public void testGetOrSetDefault(String testVal, String expectedResult) {
        String actualResult = VariableUtilities.getOrSetDefault(testVal, DEFAULT_VALUE);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
