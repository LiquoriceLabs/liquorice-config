package io.liquorice.config.formatter.passthrough;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_LIST;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_VALUE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by mthorpe on 12/11/16.
 */
public class PassThroughConfigFormatterTest {

    private PassThroughConfigFormatter configFormatter;

    @BeforeTest
    public void setup() {
        this.configFormatter = new PassThroughConfigFormatter();
    }

    @Test
    public void testReadWriteSimple() {
        final Object written = configFormatter.write(TEST_VALUE);
        final String read = configFormatter.read(written, String.class).get();
        assertEquals(read, TEST_VALUE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadWriteComplex() {
        final Object written = configFormatter.write(TEST_LIST);
        final List<String> read = (List<String>) configFormatter.read(written, List.class).get();
        assertEquals(read.size(), TEST_LIST.size());
        assertTrue(read.containsAll(TEST_LIST));
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = ClassCastException.class)
    public void testReadFailure() {
        final Object written = configFormatter.write(TEST_VALUE);
        final List<String> read = (List<String>) configFormatter.read(written, List.class).get();
        fail(String.format("Excepted ClassCastException when incorrectly casting '%s'", read));
    }
}
