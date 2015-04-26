package io.liquorice.config.formatter.passthrough;

import com.google.common.collect.ImmutableList;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by mthorpe on 12/11/16.
 */
public class PassThroughConfigFormatterTest {

    private static final String TEST_STRING = "this is a test string";
    private static final List<String> TEST_LIST = ImmutableList.of("string1", "string2");

    private PassThroughConfigFormatter configFormatter;

    @BeforeTest
    public void setup() {
        this.configFormatter = new PassThroughConfigFormatter();
    }

    @Test
    public void testReadWriteSimple() {
        final Object written = configFormatter.write(TEST_STRING);
        final String read = configFormatter.read(written, String.class);
        assertEquals(read, TEST_STRING);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadWriteComplex() {
        final Object written = configFormatter.write(TEST_LIST);
        final List<String> read = (List<String>) configFormatter.read(written, List.class);
        assertEquals(read.size(), TEST_LIST.size());
        assertTrue(read.containsAll(TEST_LIST));
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = ClassCastException.class)
    public void testReadFailure() {
        final Object written = configFormatter.write(TEST_STRING);
        final List<String> read = (List<String>) configFormatter.read(written, List.class);
        fail(String.format("Excepted ClassCastException when incorrectly casting '%s'", read));
    }
}
